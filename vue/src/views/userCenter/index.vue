<template>
  <div class="app-container">
    <el-form ref="user" :model="user" label-width="120px">
        <img :width="size" :height="size" :src="user.avatar" style="border-radius: 10px;"/>
        <el-form-item label="Username">
          <el-input v-model="user.username" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="user.email" />
        </el-form-item>
        <el-form-item label="Register Time">
          <el-input :value="unix2CurrentTime(user.registerTime)" readonly="readonly" />
        </el-form-item>
        <el-form-item label="Last Login Time">
          <el-input :value="unix2CurrentTime(user.lastLoginTime)" readonly="readonly" />
        </el-form-item>
        <el-form-item label="Resume">
          <el-input type="textarea"
                    :autosize="{ minRows: 3, maxRows: 6}"
                    v-model="user.resume" />
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="user.password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateUser">update</el-button>
          <el-button v-if="status === 'update'" @click="cancelUpdateUser">cancel</el-button>
        </el-form-item>
    </el-form>
  </div>
</template>
<script>
  import { unix2CurrentTime } from '@/utils/date'
  import { mapState } from 'vuex'

  export default {
    data() {
      return {
        dataLoading: false, // 数据加载等待动画
        btnLoading: false, // 按钮等待动画
        readonly: false, // 只读输入框
        status: null,
        size: 170
        /*
        user: {
          id: '',
          avatar: '',
          email: '',
          username: '',
          password: '',
          registerTime: '',
          lastLoginTime: '',
          resume: '',
          roleName: ''
        }
        */
      }
    },
    computed: {
      ...mapState({
        user: state => state.user
      })
    },
    methods: {
      unix2CurrentTime(timestamp) {
        return unix2CurrentTime(timestamp)
      },
      updateUser() {
        this.status = 'update'
      },
      cancelUpdateUser() {
        this.status = null
      }
    }
  }
</script>
