import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { getToken } from '@/utils/token'

const whiteList = ['/login'] // 白名单,不需要登录的路由

router.beforeEach((to, from, next) => {
  NProgress.start() // 开始Progress
  // 尝试获取cookie中的token
  if (getToken()) {
    // 有token
    if (to.path === '/login') {
      // 但下一跳是登陆页
      // 转到首页
      next({ path: '/' })
    } else {
      // 下一跳不是登陆页
      // VUEX被清除，没有角色名
      if (store.getters.roleName === null) {
        // 重新获取用户信息
        store.dispatch('Detail').then(response => {
          // 生成路由
          store.dispatch('GenerateRoutes', response.data).then(() => {
            router.addRoutes(store.getters.addRouters)
            next({ ...to })
          })
        })
      } else {
        next()
      }
    }
  } else {
    // 如果前往的路径是白名单内的,就可以直接前往
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      // 如果路径不是白名单内的,而且又没有登录,就转到登录页
      next('/login')
      NProgress.done() // 结束Progress
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
