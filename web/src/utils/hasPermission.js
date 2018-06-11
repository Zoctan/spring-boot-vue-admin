import store from '../store'

export function hasPermission(permission) {
  return store.getters.permissionCodeList.indexOf(permission) >= 0
}
