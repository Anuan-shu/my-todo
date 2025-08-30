<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>Todo 应用登录</h2>
      </template>
      
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button @click="handleRegister" :loading="loading" style="width: 100%">
            注册
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '../api'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const loginForm = ref(null)
    const loading = ref(false)
    
    const form = reactive({
      username: '',
      password: ''
    })
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }
    
    const handleLogin = async () => {
      try {
        await loginForm.value.validate()
        loading.value = true
        
        const response = await api.post('/auth/login', form)
        localStorage.setItem('token', response.data.token)
        localStorage.setItem('username', response.data.username)
        
        ElMessage.success('登录成功')
        router.push('/todos')
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
    
    const handleRegister = async () => {
      try {
        await loginForm.value.validate()
        loading.value = true
        
        await api.post('/auth/register', form)
        ElMessage.success('注册成功，请登录')
        
        // 注册成功后清空密码
        form.password = ''
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '注册失败')
      } finally {
        loading.value = false
      }
    }
    
    return {
      form,
      rules,
      loginForm,
      loading,
      handleLogin,
      handleRegister
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
}

.login-card :deep(.el-card__header) {
  text-align: center;
}

.login-card h2 {
  margin: 0;
  color: #409eff;
}
</style> 