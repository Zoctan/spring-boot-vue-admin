<template>
  <div>
    <template v-for="item in routes">
      <router-link
        v-if="!item.hidden && item.noDropDown && item.children.length > 0"
        :key="item.name"
        :to="item.path + '/' + item.children[0].path"
      >
        <el-menu-item :index="item.path + '/' + item.children[0].path">
          <icon-svg v-if="item.icon" :icon-class="item.icon" />
          {{ item.children[0].name }}
        </el-menu-item>
      </router-link>
      <el-submenu :index="item.name" :key="item.name" v-if="!item.noDropDown && !item.hidden">
        <template slot="title">
          <icon-svg v-if="item.icon" :icon-class="item.icon" />
          {{ item.name }}
        </template>
        <template v-for="child in item.children">
          <div v-if="!child.hidden" :key="child.name">
            <sidebar-item
              class="menu-indent"
              v-if="child.children && child.children.length > 0"
              :routes="[child]"
              :key="child"
            />
            <router-link v-else class="menu-indent" :to="item.path + '/' + child.path" :key="child">
              <el-menu-item :index="item.path + '/' + child.path">{{ child.name }}</el-menu-item>
            </router-link>
          </div>
        </template>
      </el-submenu>
    </template>
  </div>
</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    routes: {
      type: Array
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.svg-icon {
  margin-right: 10px;
}

.hideSidebar .menu-indent {
  display: block;
  text-indent: 10px;
}
</style>
