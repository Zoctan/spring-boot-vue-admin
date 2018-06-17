<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style lang="scss">
  // normalize.css 样式格式化
  @import '../node_modules/normalize.css/normalize.css';
  // 全局自定义的css样式
  @import './styles/index.scss';
</style>
