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
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/todos')
  } else {
    next()
  }
})

export default router 