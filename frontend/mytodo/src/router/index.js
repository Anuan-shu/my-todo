import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import TodoList from '../views/TodoList.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/todos',
    name: 'TodoList',
    component: TodoList,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAuth) {
    try {
      // 检查认证状态
      const response = await fetch('http://localhost:8080/api/auth/check', {
        credentials: 'include'
      })
      const data = await response.json()
      
      if (data.isLoggedIn) {
        next()
      } else {
        next('/login')
      }
    } catch (error) {
      next('/login')
    }
  } else if (to.path === '/login') {
    // 如果已登录，重定向到任务列表
    try {
      const response = await fetch('http://localhost:8080/api/auth/check', {
        credentials: 'include'
      })
      const data = await response.json()
      
      if (data.isLoggedIn) {
        next('/todos')
      } else {
        next()
      }
    } catch (error) {
      next()
    }
  } else {
    next()
  }
})

export default router 