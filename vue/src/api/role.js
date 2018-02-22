import fetch from '@/utils/fetch'

export function list(params) {
  return fetch({
    url: '/role',
    method: 'get',
    params
  })
}
