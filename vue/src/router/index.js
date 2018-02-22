import Vue from 'vue'
import Router from 'vue-router'
const _import = require('./_import_' + process.env.NODE_ENV)
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading

/* layout */
import Layout from '../views/layout/Layout'

Vue.use(Router)

 /**
  * icon : the icon show in the sidebar
  * hidden : if `hidden:true` will not show in the sidebar
  * redirect : if `redirect:noredirect` will not redirect in the levelbar
  * noDropdown : if `noDropdown:true` will not has submenu in the sidebar
  * meta : `{ role: ['admin'] }`  will control the page role
  **/
export const constantRouterMap = [
  { path: '/login', component: _import('login/index'), hidden: true },
  { path: '/404', component: _import('404'), hidden: true },
  {
    path: '/',
    component: Layout,
    redirect: 'dashboard',
    icon: 'zujian',
    name: '首页',
    children: [
      { path: 'dashboard', name: 'xx', icon: 'zonghe', component: _import('dashboard/index') }
    ]
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '/example',
    component: Layout,
    redirect: 'noredirect',
    icon: 'zujian',
    name: 'Example',
    children: [
      { path: 'index', name: 'Form', icon: 'zonghe', component: _import('page/form') }
    ]
  },

  {
    path: '/admin/role',
    component: Layout,
    redirect: '/admin/role/index',
    icon: 'tubiao',
    noDropdown: true,
    children: [
      { path: 'index', name: 'Role', component: _import('admin/role/index'), meta: { auth: ['role:list'] }}
    ]
  },

  {
    path: '/admin/user',
    component: Layout,
    redirect: '/admin/user/index',
    icon: 'tubiao',
    noDropdown: true,
    children: [
      { path: 'index', name: 'user', component: _import('admin/user/index'), meta: { auth: ['user:list'] }}
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]
