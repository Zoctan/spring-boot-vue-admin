import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/user',
    method: 'get',
    params
  })
}

export function update(userForm) {
  return request({
    url: '/user',
    method: 'put',
    data: userForm
  })
}

export function remove(userId) {
  return request({
    url: '/user/' + userId,
    method: 'delete'
  })
}

export function register(registerForm) {
  return request({
    url: '/user',
    method: 'post',
    data: registerForm
  })
}

export function login(loginForm) {
  return request({
    url: '/user/login',
    method: 'post',
    data: loginForm
  })
}

export function info() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'get'
  })
}
