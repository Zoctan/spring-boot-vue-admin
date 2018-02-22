import store from '../store'

export function hasPermission(auth) {
  const authList = store.getters.authList
  return authList.indexOf(auth) >= 0
}
