const getters = {
  sidebar: state => state.app.sidebar,

  token: state => state.account.token,
  accountId: state => state.account.accountId,
  email: state => state.account.email,
  name: state => state.account.name,
  loginTime: state => state.account.loginTime,
  registerTime: state => state.account.registerTime,
  roleName: state => state.account.roleName,
  permissionCodeList: state => state.account.permissionCodeList,

  permissionRouters: state => state.permission.routers,
  addRouters: state => state.permission.addRouters
}
export default getters
