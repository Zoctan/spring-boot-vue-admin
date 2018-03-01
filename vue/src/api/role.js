import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/role',
    method: 'get',
    params
  })
}

export function add(role) {
  return request({
    url: '/role',
    method: 'post',
    data: role
  })
}

export function updateUserRole(user) {
  return request({
    url: '/user/role',
    method: 'put',
    data: user
  })
}
