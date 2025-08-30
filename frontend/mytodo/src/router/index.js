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
  const userId = localStorage.getItem('userId')
  
  if (to.meta.requiresAuth && !userId) {
    next('/login')
  } else if (to.path === '/login' && userId) {
    next('/todos')
  } else {
    next()
  }
})

export default router 