<template>
  <div class="app-container">
    <el-form v-loading.body="loading"
             :model="tmpUser"
             :rules="updateInfoRules"
             ref="tmpUser"
             label-width="115px">
      <div style="text-align: center; width: 65vw; margin-bottom: 12px">
        <img width="170" height="170" :src="user.avatar" style="border-radius: 10px; margin: 0 auto;"/>
      </div>
      <el-row :gutter="18">
        <el-col :span="9">
          <el-form-item label="Username" prop="username">
            <el-input v-model="tmpUser.username"/>
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item label="Email" prop="email">
            <el-input v-model="tmpUser.email"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="18">
        <el-col :span="9">
          <el-form-item label="Register Time">
            <el-input :value="unix2CurrentTime(user.registerTime)" readonly="readonly"/>
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item label="Login Time">
            <el-input :value="unix2CurrentTime(user.loginTime)" readonly="readonly"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="18">
        <el-col :span="18">
          <el-form-item label="Resume"
                        prop="resume">
            <el-input type="textarea"
                      :autosize="{ minRows: 3, maxRows: 6}"
                      v-model="tmpUser.resume"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-row :gutter="18">
          <el-col :span="6">
            <el-button type="success"
                       :loading="btnLoading"
                       @click="regainUserInfo">regain info
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="primary"
                       :loading="btnLoading"
                       @click="updateInfo">update
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="danger"
                       @click="showUpdatePasswordDialog">update password
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>

    <el-dialog title="Update Password"
               :visible.sync="dialogFormVisible">
      <el-form status-icon class="small-space" label-position="left" label-width="115px" style="width: 400px; margin-left:50px;"
               :model="tmpPassword"
               :rules="updatePasswordRules"
               ref="tmpPassword">
        <el-form-item label="Old Password" prop="oldPassword" required>
          <el-input type="password" prefix-icon="el-icon-edit" auto-complete="off" placeholder="please input old password"
                    v-model="tmpPassword.oldPassword"/>
        </el-form-item>
        <el-form-item label="New Password"
                      prop="newPassword" required>
          <el-input type="password" prefix-icon="el-icon-edit" auto-complete="off" placeholder="please input new password"
                    v-model="tmpPassword.newPassword"/>
        </el-form-item>
        <el-form-item label="New Password" prop="newPassword2" required>
          <el-input type="password" prefix-icon="el-icon-edit" auto-complete="off" placeholder="please input new password again"
                    v-model="tmpPassword.newPassword2"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">cancel</el-button>
        <el-button type="danger"
                   @click="$refs['tmpPassword'].resetFields()">reset
        </el-button>
        <el-button type="primary"
                   :loading="btnLoading"
                   @click.native.prevent="updatePassword">update
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import store from '@/store'
import { update as updateUser, validatePassword } from '@/api/user'
import { unix2CurrentTime } from '@/utils'
import { isValidateEmail } from '@/utils/validate'
import { setToken } from '@/utils/token'
import { mapState } from 'vuex'

export default {
  created() {
    this.setInfo()
  },
  data() {
    const validateOldPassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('password must be 6 or more characters'))
      }
      // promise异步查询后端密码
      this.validateOldPassword(value).then(isValidate => {
        if (!isValidate) {
          callback(new Error('old password not right'))
        } else {
          callback()
        }
      })
    }
    const validateNewPassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('password must be 6 or more characters'))
      } else if (this.isOldNewPasswordSame()) {
        callback(new Error('old and new password must be different'))
      } else {
        callback()
      }
    }
    const validateNewPassword2 = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('password must be 6 or more characters'))
      } else if (!this.isNewPasswordSame()) {
        callback(new Error('two password are different'))
      } else {
        callback()
      }
    }
    const validateUsername = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('username must be 3 or more characters'))
      } else {
        callback()
      }
    }
    const validateEmail = (rule, value, callback) => {
      if (!isValidateEmail(value)) {
        callback(new Error('email format error'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      btnLoading: false,
      dialogFormVisible: false,
      tmpPassword: {
        oldPassword: '',
        newPassword: '',
        newPassword2: ''
      },
      updatePasswordRules: {
        oldPassword: [{ required: true, trigger: 'blur', validator: validateOldPassword }],
        newPassword: [{ required: true, trigger: 'blur', validator: validateNewPassword }],
        newPassword2: [{ required: true, trigger: 'blur', validator: validateNewPassword2 }]
      },
      tmpUser: {
        id: '',
        username: '',
        email: '',
        resume: ''
      },
      updateInfoRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        email: [{ required: true, trigger: 'blur', validator: validateEmail }]
      }
    }
  },
  computed: {
    ...mapState({
      user: state => state.user
    })
  },
  methods: {
    unix2CurrentTime,
    /**
     * 设置用户资料
     */
    setInfo() {
      this.tmpUser.username = this.user.username
      this.tmpUser.email = this.user.email
      this.tmpUser.resume = this.user.resume
    },
    /**
     * 验证旧密码
     * @param oldPassword 旧密码
     */
    validateOldPassword(oldPassword) {
      const user = {
        id: this.user.userId,
        password: oldPassword
      }
      return validatePassword(user).then(response => response.data)
    },
    /**
     * 新旧密码是否相同
     */
    isOldNewPasswordSame() {
      return this.tmpPassword.oldPassword === this.tmpPassword.newPassword
    },
    /**
     * 新密码1和2是否相同
     */
    isNewPasswordSame() {
      return this.tmpPassword.newPassword === this.tmpPassword.newPassword2
    },
    /**
     * 重置token
     */
    resetToken(token) {
      setToken(token)
      this.user.token = token
    },
    /**
     * 重新获取用户信息
     */
    regainUserInfo() {
      this.loading = true
      this.btnLoading = true
      store.dispatch('Info').then(() => {
        this.loading = false
        this.btnLoading = false
      })
    },
    /**
     * 更新用户
     * @param user 用户
     */
    updateUser(user) {
      this.btnLoading = true
      updateUser(user).then(response => {
        this.$message.success('update success')
        this.resetToken(response.data)
        this.regainUserInfo()
        this.btnLoading = false
      })
    },
    /**
     * 更新用户资料
     */
    updateInfo() {
      this.$refs.tmpUser.validate(valid => {
        if (valid) {
          this.tmpUser.id = this.user.userId
          this.updateUser(this.tmpUser)
        }
      })
    },
    /**
     * 显示更新密码对话框
     */
    showUpdatePasswordDialog() {
      this.dialogFormVisible = true
      this.tmpPassword.oldPassword = ''
      this.tmpPassword.newPassword = ''
      this.tmpPassword.newPassword2 = ''
    },
    /**
     * 更新密码
     */
    updatePassword() {
      this.$refs.tmpPassword.validate(valid => {
        if (valid) {
          const user = {}
          user.id = this.user.userId
          user.password = this.tmpPassword.newPassword
          this.updateUser(user)
          this.dialogFormVisible = false
        }
      })
    }
  }
}
</script>
