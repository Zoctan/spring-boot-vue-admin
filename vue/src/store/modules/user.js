import { login, logout, info } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/token'

const user = {
  state: {
    token: getToken(),
    userId: -1,
    email: null,
    username: null,
    avatar: null,
    lastLoginTime: -1,
    registerTime: -1,
    resume: null,
    roleName: null,
    permissionCodeList: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USER: (state, user) => {
      state.userId = user.id
      state.email = user.email
      state.username = user.username
      state.avatar = user.avatar
      state.lastLoginTime = user.lastLoginTime
      state.registerTime = user.registerTime
      state.resume = user.resume
      state.roleName = user.roleName
      state.permissionCodeList = user.permissionCodeList
    },
    RESET_USER: (state) => {
      state.token = null
      state.userId = -1
      state.email = null
      state.username = null
      state.avatar = null
      state.lastLoginTime = -1
      state.registerTime = -1
      state.resume = null
      state.roleName = null
      state.permissionCodeList = []
    }
  },

  actions: {
    // 登录
    Login({ commit }, loginForm) {
      return new Promise((resolve, reject) => {
        login(loginForm).then(data => {
          if (data.status === 200) {
            // cookie中保存前端登录状态
            setToken(data.data)
            commit('SET_TOKEN', data.data)
          }
          // 传递给/login/index.vue : store.dispatch('Login').then(data)
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    Info({ commit }) {
      return new Promise((resolve, reject) => {
        info().then(response => {
          const data = response.data
          // 储存用户信息
          commit('SET_USER', data)
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout({ commit }) {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          // 清除token等相关角色信息
          commit('RESET_USER')
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogout({ commit }) {
      return new Promise(resolve => {
        commit('RESET_USER')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
