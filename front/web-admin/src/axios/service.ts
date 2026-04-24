import axios, { AxiosError } from 'axios'
import { defaultRequestInterceptors, defaultResponseInterceptors } from './config'

import { AxiosInstance, InternalAxiosRequestConfig, RequestConfig, AxiosResponse } from './types'
import { ElMessage } from 'element-plus'
import { REQUEST_TIMEOUT } from '@/constants'
import { refreshTokenApi } from '@/api/login'
import { useUserStoreWithOut } from '@/store/modules/user'

export const PATH_URL = import.meta.env.VITE_API_BASE_PATH

const abortControllerMap: Map<string, AbortController> = new Map()

// Token 刷新相关变量
let isRefreshing = false
type PendingRequest = {
  resolve: (value: unknown) => void
  reject: (reason?: unknown) => void
  config: InternalAxiosRequestConfig
}
let pendingRequests: PendingRequest[] = []

const axiosInstance: AxiosInstance = axios.create({
  // 允许携带cookie
  withCredentials: true,
  timeout: REQUEST_TIMEOUT,
  baseURL: PATH_URL
})

/**
 * 重试所有挂起的请求
 * @param newToken 新的 Token
 */
const retryPendingRequests = (newToken: string) => {
  pendingRequests.forEach(({ config, resolve }) => {
    // 更新请求头中的 token
    config.headers.Authorization = `Bearer ${newToken}`
    axiosInstance
      .request(config)
      .then(resolve)
      .catch(() => {})
  })
  pendingRequests = []
}

axiosInstance.interceptors.request.use((res: InternalAxiosRequestConfig) => {
  // 如果请求配置中没有明确设置 withCredentials，默认为 true
  if (res.withCredentials === undefined) {
    res.withCredentials = true
  }
  const controller = new AbortController()
  const url = res.url || ''
  res.signal = controller.signal
  abortControllerMap.set(url, controller)
  return res
})

axiosInstance.interceptors.response.use(
  (res: AxiosResponse) => {
    const url = res.config.url || ''
    abortControllerMap.delete(url)
    // 这里不能做任何处理，否则后面的 interceptors 拿不到完整的上下文了
    return res
  },
  async (error: AxiosError) => {
    console.error('err： ', error) // for debug
    // 处理 401 错误（Token 过期）
    if (error.response?.status === 401) {
      const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

      // 如果是刷新 token 的请求本身失败，直接退出登录
      if (originalRequest.url?.includes('/sign-in')) {
        const userStore = useUserStoreWithOut()
        userStore.logout()
        ElMessage.error((error.response.data as any).clientMessage)
        return Promise.reject(error)
      }
      if (originalRequest.url?.includes('/auth/refresh')) {
        const userStore = useUserStoreWithOut()
        userStore.logout()
        return Promise.reject(error)
      }

      // 如果已经在刷新 token，将当前请求加入等待队列
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          pendingRequests.push({
            resolve,
            reject,
            config: originalRequest
          })
        })
      }

      // 标记正在刷新
      isRefreshing = true
      originalRequest._retry = true

      try {
        // 尝试刷新 token
        const res = await refreshTokenApi()

        if (res && res.data) {
          const userStore = useUserStoreWithOut()
          const newToken = res.data.accessToken
          userStore.setToken(newToken)

          // 更新原始请求的 token
          originalRequest.headers.Authorization = `Bearer ${newToken}`

          // 重试所有挂起的请求
          retryPendingRequests(newToken)

          // 重试当前请求
          return axiosInstance.request(originalRequest)
        } else {
          // 刷新失败，退出登录
          const userStore = useUserStoreWithOut()
          userStore.logout()
          return Promise.reject(new Error('Token 刷新失败'))
        }
      } catch (refreshError) {
        // 刷新失败，退出登录
        console.error('Token 刷新失败:', refreshError)
        const userStore = useUserStoreWithOut()
        userStore.logout()
        return Promise.reject(refreshError)
      } finally {
        // 重置刷新标志
        isRefreshing = false
      }
    }

    // 其他错误处理
    // ⚠️ 关键修改：只有非 401 错误才显示错误提示
    // 401 错误要么被刷新处理了，要么会退出登录，不需要显示错误
    if (error.response?.status !== 401) {
      if (error.response && error.response.data) {
        // 检查是否是已经重试过的请求（_retry 标记）
        const config = error.config as InternalAxiosRequestConfig & { _retry?: boolean }
        if (!config?._retry) {
          ElMessage.error((error.response.data as any).clientMessage || '未知错误')
        }
      } else {
        ElMessage.error(error.message || '请求失败，请检查网络')
      }
    }

    return Promise.reject(error)
  }
)

axiosInstance.interceptors.request.use(defaultRequestInterceptors)
axiosInstance.interceptors.response.use(defaultResponseInterceptors)

const service = {
  request: (config: RequestConfig) => {
    return new Promise((resolve, reject) => {
      if (config.interceptors?.requestInterceptors) {
        config = config.interceptors.requestInterceptors(config as any)
      }

      axiosInstance
        .request(config)
        .then((res) => {
          resolve(res)
        })
        .catch((err: any) => {
          reject(err)
        })
    })
  },
  cancelRequest: (url: string | string[]) => {
    const urlList = Array.isArray(url) ? url : [url]
    for (const _url of urlList) {
      abortControllerMap.get(_url)?.abort()
      abortControllerMap.delete(_url)
    }
  },
  cancelAllRequest() {
    for (const [_, controller] of abortControllerMap) {
      controller.abort()
    }
    abortControllerMap.clear()
  }
}

export default service
