import Vue from 'vue'
import ElementUI from 'element-ui'
import './styles/element-variables.scss'
import locale from 'element-ui/lib/locale/lang/en'
import App from './App'
import router from './router'
import store from './store'
import '@/icons' // icon
import '@/permission' // 权限
import { default as fetch } from './utils/fetch'
import { hasPermission } from './utils/hasPermission'

Vue.use(ElementUI, { locale })

// 全局的常量
Vue.prototype.fetch = fetch
Vue.prototype.hasPermission = hasPermission

// 生产环境时自动设置为 false 以阻止 vue 在启动时生成生产提示。
Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
