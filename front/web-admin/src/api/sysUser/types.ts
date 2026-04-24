export type SysUserType = {
  id: string
  username: string
  password: string
  roles: string[]
  avatar: string
  nickname: string
  mobile: string
  email: string
  gender: number
  validTime: string
  enabled: boolean
  locked: boolean
  lastLoginTime: string
  remark: string
  deleted: boolean
  createTime: string
  updateTime: string
}

export type AddSysUserType = {
  username: string
  password: string
  roles: string[]
}
