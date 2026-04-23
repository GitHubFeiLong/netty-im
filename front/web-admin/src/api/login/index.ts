import request from '@/axios'
import { UserDetails, UserType } from './types'

interface RoleParams {
  roleName: string
}

export const loginApi = (data: UserType): Promise<IResponse<UserType>> => {
  return request.post({ url: '/auth/sys/sign-in', data })
}

export const userDetailsApi = (): Promise<IResponse<UserDetails>> => {
  return request.get({ url: '/auth/user-details' })
}

export const refreshTokenApi = (): Promise<IResponse<UserDetails>> => {
  return request.post({ url: '/auth/refresh' })
}

export const loginOutApi = (): Promise<IResponse> => {
  return request.post({ url: '/auth/sign-out' })
}

export const getAdminRoleApi = (
  params: RoleParams
): Promise<IResponse<AppCustomRouteRecordRaw[]>> => {
  return request.get({ url: '/mock/role/list', params })
}

export const getTestRoleApi = (params: RoleParams): Promise<IResponse<string[]>> => {
  return request.get({ url: '/mock/role/list2', params })
}
