<template>
  <div class="login-container">
    <el-form
      class="card-box login-form"
      autocomplete="on"
      :model="loginForm"
      :rules="loginRules"
      ref="loginForm"
      status-icon
      label-position="left"
      label-width="0px"
    >
      <h3 class="title">后台登录</h3>
      <el-form-item prop="nameOrEmail">
        <span class="svg-container svg-container_login">
          <icon-svg icon-class="name" />
        </span>
        <el-input
          type="text"
          autocomplete="on"
          v-model="loginForm.nameOrEmail"
          placeholder="请输入账户名或邮箱"
          @keyup.enter.native="handleLogin"
        />
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <icon-svg icon-class="password" />
        </span>
        <el-input
          :type="passwordType"
          autocomplete="on"
          v-model="loginForm.password"
          placeholder="请输入密码"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click.native.prevent="showPwd">
          <icon-svg icon-class="eye" />
        </span>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          style="width:100%;"
          :loading="btnLoading"
          @click.native.prevent="handleLogin"
        >登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { isValidateEmail } from '@/utils/validate'

export default {
  name: 'login',
  data() {
    const validateNameOrEmail = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('账户名长度必须在3或以上'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码长度必须在6或以上'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      loginForm: {
        nameOrEmail: 'admin',
        password: 'admin123'
      },
      loginRules: {
        nameOrEmail: [
          { required: true, trigger: 'blur', validator: validateNameOrEmail }
        ],
        password: [
          { required: true, trigger: 'blur', validator: validatePassword }
        ]
      },
      passwordType: 'password',
      btnLoading: false
    }
  },
  methods: {
    showPwd() {
      this.passwordType =
        this.passwordType === 'password' ? '' : (this.passwordType = 'password')
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          const account = {}
          if (isValidateEmail(this.loginForm.nameOrEmail)) {
            account.email = this.loginForm.nameOrEmail
          } else {
            account.name = this.loginForm.nameOrEmail
          }
          account.password = this.loginForm.password
          this.loading = true
          this.$store.dispatch('Login', account).then(() => {
            this.loading = false
            this.$router.push({ path: '/dashboard' })
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import '../../../src/styles/mixin.scss';

$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  @include relative;
  height: 100vh;
  overflow-y: hidden;
  background-color: $bg;
  input:-webkit-autofill {
    -webkit-box-shadow: 0 0 0 1000px #293444 inset !important;
    -webkit-text-fill-color: #fff !important;
  }
  input {
    background: transparent;
    border: 0;
    -webkit-appearance: none;
    border-radius: 0;
    padding: 12px 5px 12px 15px;
    color: $light_gray;
    height: 47px;
  }
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
  }
  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
    &_login {
      font-size: 20px;
    }
  }
  .title {
    font-size: 26px;
    color: $light_gray;
    margin: 0 auto 40px auto;
    text-align: center;
    font-weight: bold;
  }
  .login-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 400px;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
  }
}
</style>
