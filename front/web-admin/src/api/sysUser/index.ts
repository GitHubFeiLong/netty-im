import request from '@/axios'
import { AddSysUserType, SysUserType } from './types'

export const saveSysUserApi = (data: AddSysUserType): Promise<IResponse<SysUserType>> => {
  return request.post({ url: '/sys/user', data })
}

export const pageApi = (data: any): Promise<IResponse<SysUserType>> => {
  return request.post({ url: '/sys-user/page', data })
}
