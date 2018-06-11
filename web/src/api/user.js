import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/user',
    method: 'get',
    params
  })
}

export function validatePassword(userForm) {
  return request({
    url: '/user/password',
    method: 'post',
    data: userForm
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

export function register(userForm) {
  return request({
    url: '/user',
    method: 'post',
    data: userForm
  })
}

export function login(userForm) {
  return request({
    url: '/user/login',
    method: 'post',
    data: userForm
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
