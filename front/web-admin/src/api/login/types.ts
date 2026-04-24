export type UserLoginType = {
  username: string
  password: string
}

export type UserType = {
  accessToken: string
  username: string
  password: string
  rememberMe: boolean
  roles: string[]
  role: string
  roleId: string
  permissions: string | string[]
}

export type UserDetails = {
  im: any
  sys: any
}

export type TokenType = {
  accessExpires: string
  accessToken: string
  tokenType: string
}
