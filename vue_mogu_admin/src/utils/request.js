import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '../store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: '', // api 的 base_url
  timeout: 10000 // 请求超时时间
})

service.defaults.headers.common['Authorization'] = getToken()

// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      // config.headers['X-Token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改 
      config.headers.Authorization = getToken()
    }
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    // /**
    //  * code为非20000是抛错 可结合自己业务进行修改
    //  */
    const res = response.data
    if (res.code === 'success' || res.code === 'error') {
      return response.data
    } else {
      console.log("错误信息", res.message)

      Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
      })

      return Promise.reject('error')

      // MessageBox.confirm(
      //   '你已被登出，可以取消继续留在该页面，或者重新登录',
      //   '确定登出',
      //   {
      //     confirmButtonText: '重新登录',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }
      // ).then(() => {
      //   store.dispatch('FedLogOut').then(() => {
      //     location.reload() // 为了重新实例化vue-router对象 避免bug
      //   })
      // })
      // return Promise.reject('error')
    }
  },
  error => {
    console.log('错误码', error) // for debug    
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
