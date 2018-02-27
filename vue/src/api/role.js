import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/role',
    method: 'get',
    params
  })
}
