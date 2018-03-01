import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/permission',
    method: 'get',
    params
  })
}
