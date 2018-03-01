<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      <h4>用户权限Code</h4>
      <ol>
        <li v-for="code in permissionCodeList" :key="permission">{{ code }}</li>
      </ol>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'dashboard',
  computed: {
    ...mapGetters([
      'permissionCodeList'
    ])
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
