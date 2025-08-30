<template>
  <div class="todo-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>我的待办事项</h1>
          <div class="user-info">
            <span>欢迎，{{ username }}</span>
            <el-button @click="logout" type="danger" size="small">退出登录</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <!-- 筛选和添加 -->
        <div class="toolbar">
          <div class="filters">
            <el-select v-model="statusFilter" placeholder="状态筛选" @change="loadTodos">
              <el-option label="全部" value="ALL" />
              <el-option label="未完成" value="PENDING" />
              <el-option label="已完成" value="COMPLETED" />
            </el-select>
          </div>
          
          <el-button type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon>
            添加任务
          </el-button>
        </div>
        
        <!-- Todo列表 -->
        <el-table :data="todos" style="width: 100%" v-loading="loading">
          <el-table-column prop="title" label="标题" min-width="200">
            <template #default="{ row }">
              <span :class="{ 'completed': row.status === 'COMPLETED' }">
                {{ row.title }}
              </span>
            </template>
          </el-table-column>
          
          <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
          
          <el-table-column prop="deadline" label="截止时间" width="180">
            <template #default="{ row }">
              <el-tag v-if="row.deadline" :type="getDeadlineTagType(row.deadline)">
                {{ formatDateTime(row.deadline) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="priority" label="优先级" width="100">
            <template #default="{ row }">
              <el-tag :type="getPriorityTagType(row.priority)">
                {{ getPriorityText(row.priority) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-switch
                v-model="row.status"
                :active-value="'COMPLETED'"
                :inactive-value="'PENDING'"
                @change="updateTodoStatus(row)"
              />
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button size="small" @click="editTodo(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="deleteTodo(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
    
    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingTodo ? '编辑任务' : '添加任务'"
      width="500px"
    >
      <el-form :model="todoForm" :rules="todoRules" ref="todoFormRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="todoForm.title" placeholder="请输入任务标题" />
        </el-form-item>
        
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="todoForm.content"
            type="textarea"
            :rows="3"
            placeholder="请输入任务内容"
          />
        </el-form-item>
        
        <el-form-item label="截止时间" prop="deadline">
          <el-date-picker
            v-model="todoForm.deadline"
            type="datetime"
            placeholder="选择截止时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="todoForm.priority" placeholder="选择优先级">
            <el-option label="低" value="LOW" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="高" value="HIGH" />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveTodo" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import api from '../api'

export default {
  name: 'TodoList',
  components: { Plus },
  setup() {
    const router = useRouter()
    const loading = ref(false)
    const saving = ref(false)
    const todos = ref([])
    const statusFilter = ref('ALL')
    const showAddDialog = ref(false)
    const editingTodo = ref(null)
    const todoFormRef = ref(null)
    
    const username = computed(() => localStorage.getItem('username') || '用户')
    
    const todoForm = reactive({
      title: '',
      content: '',
      deadline: '',
      priority: 'MEDIUM'
    })
    
    const todoRules = {
      title: [
        { required: true, message: '请输入任务标题', trigger: 'blur' }
      ]
    }
    
    const loadTodos = async () => {
      try {
        loading.value = true
        let response
        
        if (statusFilter.value === 'ALL') {
          response = await api.get('/todos')
        } else {
          response = await api.get(`/todos/status/${statusFilter.value}`)
        }
        
        todos.value = response.data
      } catch (error) {
        ElMessage.error('加载任务列表失败')
      } finally {
        loading.value = false
      }
    }
    
    const checkUpcomingTodos = async () => {
      try {
        const response = await api.get('/todos/upcoming')
        const upcomingTodos = response.data
        
        if (upcomingTodos.length > 0) {
          ElMessageBox.alert(
            `您有 ${upcomingTodos.length} 个任务将在24小时内到期，请及时处理！`,
            '任务提醒',
            { confirmButtonText: '知道了' }
          )
        }
      } catch (error) {
        console.error('检查即将到期的任务失败:', error)
      }
    }
    
    const saveTodo = async () => {
      try {
        await todoFormRef.value.validate()
        saving.value = true
        
        if (editingTodo.value) {
          await api.put(`/todos/${editingTodo.value.id}`, todoForm)
          ElMessage.success('任务更新成功')
        } else {
          await api.post('/todos', todoForm)
          ElMessage.success('任务添加成功')
        }
        
        showAddDialog.value = false
        resetForm()
        loadTodos()
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '保存失败')
      } finally {
        saving.value = false
      }
    }
    
    const editTodo = (todo) => {
      editingTodo.value = todo
      todoForm.title = todo.title
      todoForm.content = todo.content || ''
      todoForm.deadline = todo.deadline ? dayjs(todo.deadline).format('YYYY-MM-DD HH:mm:ss') : ''
      todoForm.priority = todo.priority
      showAddDialog.value = true
    }
    
    const deleteTodo = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这个任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await api.delete(`/todos/${id}`)
        ElMessage.success('删除成功')
        loadTodos()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }
    
    const updateTodoStatus = async (todo) => {
      try {
        await api.put(`/todos/${todo.id}`, {
          status: todo.status
        })
        ElMessage.success('状态更新成功')
      } catch (error) {
        ElMessage.error('状态更新失败')
        // 恢复原状态
        todo.status = todo.status === 'COMPLETED' ? 'PENDING' : 'COMPLETED'
      }
    }
    
    const resetForm = () => {
      editingTodo.value = null
      todoForm.title = ''
      todoForm.content = ''
      todoForm.deadline = ''
      todoForm.priority = 'MEDIUM'
    }
    
    const logout = () => {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      router.push('/login')
    }
    
    const formatDateTime = (dateTime) => {
      return dayjs(dateTime).format('MM-DD HH:mm')
    }
    
    const getPriorityTagType = (priority) => {
      const types = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return types[priority] || 'info'
    }
    
    const getPriorityText = (priority) => {
      const texts = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高'
      }
      return texts[priority] || '中'
    }
    
    const getDeadlineTagType = (deadline) => {
      const now = dayjs()
      const deadlineTime = dayjs(deadline)
      
      if (deadlineTime.isBefore(now)) {
        return 'danger'
      } else if (deadlineTime.isBefore(now.add(24, 'hour'))) {
        return 'warning'
      }
      return 'success'
    }
    
    onMounted(() => {
      loadTodos()
      checkUpcomingTodos()
    })
    
    return {
      loading,
      saving,
      todos,
      statusFilter,
      showAddDialog,
      editingTodo,
      todoFormRef,
      todoForm,
      todoRules,
      username,
      loadTodos,
      saveTodo,
      editTodo,
      deleteTodo,
      updateTodoStatus,
      logout,
      formatDateTime,
      getPriorityTagType,
      getPriorityText,
      getDeadlineTagType
    }
  }
}
</script>

<style scoped>
.todo-container {
  min-height: 100vh;
}

.el-header {
  background: #409eff;
  color: white;
  line-height: 60px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h1 {
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filters {
  display: flex;
  gap: 10px;
}

.completed {
  text-decoration: line-through;
  color: #999;
}

.el-main {
  padding: 20px;
}
</style> 