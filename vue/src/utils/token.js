import Cookies from 'js-cookie'

const TokenKey = 'User-Token'
// 1 day
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
