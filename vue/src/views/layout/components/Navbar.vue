<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened" />
    <Levelbar />
    <el-dropdown class="user-container" trigger="click">
      <div class="avatar-wrapper">
        <img class="user-avatar" :src="avatar + '?imageView2/1/w/80/h/80'">
        <div class="user-name">{{ username }}</div>
        <i class="el-icon-caret-bottom"></i>
      </div>
      <el-dropdown-menu slot="dropdown">
        <router-link class="inlineBlock" to="/user/center">
          <el-dropdown-item>user center</el-dropdown-item>
        </router-link>
        <el-dropdown-item divided>
          <span @click="logout" style="display:block;">logout</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-menu>
</template>

<script>
  import { mapGetters } from 'vuex'
  import Levelbar from './Levelbar'
  import Hamburger from '@/components/Hamburger'

  export default {
    components: {
      Levelbar,
      Hamburger
    },
    computed: {
      ...mapGetters([
        'username',
        'sidebar',
        'avatar'
      ])
    },
    methods: {
      toggleSideBar() {
        this.$store.dispatch('ToggleSideBar')
      },
      logout() {
        this.$store.dispatch('Logout').then(() => {
          location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .navbar {
    height: 55px;
    line-height: 55px;
    border-radius: 0 !important;
    .hamburger-container {
      line-height: 58px;
      height: 55px;
      float: left;
      padding: 0 10px;
    }
    .errLog-container {
      display: inline-block;
      position: absolute;
      right: 150px;
    }
    .user-container {
      height: 45px;
      display: inline-block;
      position: absolute;
      right: 35px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 2px;
        position: relative;
        .user-avatar {
          width: 25px;
          height: 25px;
          border-radius: 10px;
        }
        .user-name {
        position: absolute;
        top: 15px;
        font-size: 13px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 20px;
          font-size: 12px;
        }
      }
    }
  }
</style>

