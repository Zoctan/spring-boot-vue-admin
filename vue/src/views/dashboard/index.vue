<template>
  <div class="dashboard-container">
    <div class='dashboard-text'>
      <h4>用户权限</h4>
      <ol>
        <li v-for='auth in authList' :key='auth'>{{ auth }}</li>
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
      'authList'
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
