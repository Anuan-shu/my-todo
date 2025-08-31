import axios from 'axios'

// 根据环境判断API基础路径
const isDevelopment = process.env.NODE_ENV === 'development' || import.meta.env.DEV
const API_BASE_URL = isDevelopment 
  ? 'http://localhost:8080/api' 
  : '/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const userId = localStorage.getItem('userId')
    if (userId) {
      config.headers['X-User-ID'] = userId
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      // 清除本地存储
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api 