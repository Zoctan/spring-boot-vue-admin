<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPermission('user:add')" @click.native.prevent="showCreate">add</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="userList" v-loading.body="listLoading" element-loading-text="loading" border fit highlight-current-row>
      <el-table-column align="center" label="Index">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="Username" prop="username" />
      <el-table-column align="center" label="Role" >
        <template slot-scope="scope">
          <el-tag type="success" v-text="scope.row.roleName" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="Admin" v-if="hasPermission('user:update') || hasPermission('user:delete')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" v-if="hasPermission('user:update') && scope.row.username !== username" @click.native.prevent="showUpdate(scope.$index)">update</el-button>
          <el-button type="danger" icon="delete" v-if="hasPermission('user:delete') && scope.row.username !== username" @click.native.prevent="removeUser(scope.$index)">delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.page"
      :page-size="listQuery.size"
      :total="total"
      :page-sizes="[10, 30, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tmpUser" :rules="createRules" ref="tmpUser" label-position="left" label-width="75px" style='width: 300px; margin-left:50px;'>
        <el-form-item label="Email" prop="email" required>
          <el-input type="text" v-model="tmpUser.email" :readonly="readonly" />
        </el-form-item>
        <el-form-item label="Username" prop="username" required>
          <el-input type="text" v-model="tmpUser.username" :readonly="readonly" />
        </el-form-item>
        <el-form-item label="Password" prop="password" v-if="dialogStatus === 'create'" required>
          <el-input type="password" v-model="tmpUser.password" :readonly="readonly" />
        </el-form-item>
        <el-form-item label="Role" required>
          <el-select v-model="tmpUser.roleId" placeholder="please select">
            <el-option v-for="item in allRole"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">cancel</el-button>
        <el-button type="success" v-if="dialogStatus === 'create'" :loading="btnLoading" @click.native.prevent="createUser">create</el-button>
        <el-button type="primary" v-else :loading="btnLoading" @click.native.prevent="updateUserRole">update</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { list as getUserList, register, remove } from '@/api/user'
import { list as getRoleList, updateUserRole } from '@/api/role'
import { isValidateEmail } from '@/utils/validate'
import { mapGetters } from 'vuex'

export default {
  created() {
    if (this.hasPermission('role:update')) {
      this.getAllRole()
    }
    if (this.hasPermission('user:list')) {
      this.getUserList()
    }
  },
  data() {
    const validateEmail = (rule, value, callback) => {
      if (!isValidateEmail(value)) {
        callback(new Error('email format error'))
      } else {
        callback()
      }
    }
    const validateUsername = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('username length not less than 3'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('password length not less than 6'))
      } else {
        callback()
      }
    }
    return {
      userList: [], // 用户列表
      allRole: [], // 全部角色
      listLoading: false, // 数据加载等待动画
      total: 0, // 数据总数
      listQuery: {
        page: 1, // 页码
        size: 30 // 每页数量
      },
      dialogStatus: 'create',
      dialogFormVisible: false,
      textMap: {
        update: 'Update User Role',
        create: 'Create User'
      },
      btnLoading: false, // 按钮等待动画
      readonly: false, // 只读输入框
      tmpUser: {
        id: '',
        email: '',
        username: '',
        password: '',
        roleId: 2 // 对应后端数据库普通用户角色Id
      },
      createRules: {
        email: [{ required: true, trigger: 'blur', validator: validateEmail }],
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      }
    }
  },
  computed: {
    ...mapGetters([
      'username'
    ])
  },
  methods: {
    getAllRole() {
      getRoleList().then(response => {
        this.allRole = response.data.list
      })
    },
    getUserList() {
      this.listLoading = true
      getUserList(this.listQuery).then(response => {
        this.userList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleSizeChange(size) {
      // 改变每页数量
      this.listQuery.size = size
      this.handleFilter()
    },
    handleCurrentChange(page) {
      // 改变页码
      this.listQuery.page = page
      this.getUserList()
    },
    handleFilter() {
      // 查询事件
      this.listQuery.page = 1
      this.getUserList()
    },
    getIndex(index) {
      // 表格序号
      return (this.listQuery.page - 1) * this.listQuery.size + index + 1
    },
    showCreate() {
      // 显示新增对话框
      this.dialogFormVisible = true
      this.dialogStatus = 'create'
      this.tmpUser.email = ''
      this.tmpUser.username = ''
      this.tmpUser.password = ''
      this.readonly = false
    },
    createUser() {
      this.$refs.tmpUser.validate(valid => {
        if (valid) {
          this.btnLoading = true
          register(this.tmpUser).then(response => {
            if (response.status === 200) {
              this.$message.success('add success')
              this.getUserList()
              this.dialogFormVisible = false
            } else {
              this.$message.error(response.message)
            }
            this.btnLoading = false
          })
        } else {
          console.log('form not validate')
          return false
        }
      })
    },
    showUpdate(index) {
      // 显示修改对话框
      this.dialogFormVisible = true
      this.dialogStatus = 'update'
      this.tmpUser.id = this.userList[index].id
      this.tmpUser.email = this.userList[index].email
      this.tmpUser.username = this.userList[index].username
      this.tmpUser.password = ''
      this.tmpUser.roleId = this.userList[index].roleId
      this.readonly = true
    },
    updateUserRole() {
      updateUserRole(this.tmpUser).then(data => {
        if (data.status === 200) {
          this.$message.success('update success')
          this.getUserList()
          this.dialogFormVisible = false
        } else {
          this.$message.error(data.message)
        }
      })
    },
    removeUser(index) {
      this.$confirm('delete this user?', 'tip', {
        confirmButtonText: 'yes',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        const id = this.userList[index].id
        remove(id).then(response => {
          console.info(response)
          if (response.status === 200) {
            this.$message.success('delete success')
            this.getUserList()
          } else {
            this.$message.error('delete failed')
          }
        })
      })
    }
  }
}
</script>
