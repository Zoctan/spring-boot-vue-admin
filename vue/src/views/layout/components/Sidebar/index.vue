<template>
  <!-- http://element-cn.eleme.io/#/zh-CN/component/menu -->
  <el-menu mode="vertical" :default-active="$route.path">
    <sidebar-item :routes="permission_routers" />
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
export default {
  components: { SidebarItem },
  computed: {
    ...mapGetters([
      'permission_routers'
    ])
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-menu {
  min-height: 100%;
}
</style>
