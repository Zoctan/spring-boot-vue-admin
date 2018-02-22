import fetch from '@/utils/fetch'

export function list(params) {
  return fetch({
    url: '/user',
    method: 'get',
    params
  })
}

export function update(userForm) {
  return fetch({
    url: '/user',
    method: 'post',
    data: userForm
  })
}

export function remove(params) {
  return fetch({
    url: '/user',
    method: 'delete',
    params
  })
}

export function register(registerForm) {
  return fetch({
    url: '/user',
    method: 'post',
    data: registerForm
  })
}

export function login(loginForm) {
  return fetch({
    url: '/user/login',
    method: 'post',
    data: loginForm
  })
}

export function getInfo() {
  return fetch({
    url: '/user/info',
    method: 'get'
  })
}

export function logout() {
  return fetch({
    url: '/user/logout',
    method: 'get'
  })
}
