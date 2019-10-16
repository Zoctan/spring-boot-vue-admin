import Cookies from 'js-cookie'

const TokenKey = 'Account-Token'
// 有效期 1 天
const expires = 1

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token, { expires: expires })
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
