import Vue from 'vue'
import IconSvg from '@/components/Icon-svg'// svg组件

// 注册全局组件
Vue.component('icon-svg', IconSvg)

const requireAll = requireContext => requireContext.keys().map(requireContext)
const req = require.context('./svg', false, /\.svg$/)
requireAll(req)
