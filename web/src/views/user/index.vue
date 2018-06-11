<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="success"
                     icon="el-icon-refresh"
                     v-if="hasPermission('user:list')"
                     @click.native.prevent="getUserList">refresh
          </el-button>
          <el-button type="primary"
                     icon="el-icon-plus"
                     v-if="hasPermission('user:add')"
                     @click.native.prevent="showCreate">add
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="userList"
              v-loading.body="listLoading"
              element-loading-text="loading"
              border fit highlight-current-row>
      <el-table-column label="#"
                       align="center"
                       width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column label="Username"
                       align="center"
                       prop="username"/>
      <el-table-column label="Email"
                       align="center"
                       prop="email"/>
      <el-table-column label="Register Time"
                       align="center"
                       prop="registerTime">
        <template slot-scope="scope">
          {{ unix2CurrentTime(scope.row.registerTime) }}
        </template>
      </el-table-column>
      <el-table-column label="Last Login Time"
                       align="center"
                       prop="lastLoginTime">
        <template slot-scope="scope">
          {{ unix2CurrentTime(scope.row.lastLoginTime) }}
        </template>
      </el-table-column>
      <el-table-column label="Role"
                       align="center"
                       prop="roleName"
                       :filters="filterRoleNameList"
                       :filter-method="filterRoleName">
        <template slot-scope="scope">
          <el-tag type="success">{{ scope.row.roleName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Admin"
                       align="center"
                       v-if="hasPermission('role:update') || hasPermission('user:delete')">
        <template slot-scope="scope">
          <div style="margin-bottom: 10px">
            <el-button type="primary"
                       size="mini"
                       icon="el-icon-edit-outline"
                       v-if="hasPermission('role:update') && scope.row.id !== userId"
                       @click.native.prevent="showUpdate(scope.$index)">update
            </el-button>
          </div>
          <div>
            <el-button type="danger"
                       size="mini"
                       icon="el-icon-delete"
                       v-if="hasPermission('user:delete') && scope.row.id !== userId"
                       @click.native.prevent="removeUser(scope.$index)">delete
            </el-button>
          </div>
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
    <el-dialog :title="textMap[dialogStatus]"
               :visible.sync="dialogFormVisible">
      <el-form class="small-space"
               :model="tmpUser"
               :rules="createRules"
               ref="tmpUser"
               status-icon
               label-position="left"
               label-width="75px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="Email"
                      prop="email" required>
          <el-input type="text"
                    prefix-icon="el-icon-message"
                    auto-complete="off"
                    :readonly="readonly"
                    v-model="tmpUser.email"/>
        </el-form-item>
        <el-form-item label="Username"
                      prop="username" required>
          <el-input type="text"
                    prefix-icon="el-icon-edit"
                    auto-complete="off"
                    :readonly="readonly"
                    v-model="tmpUser.username"/>
        </el-form-item>
        <el-form-item label="Password"
                      prop="password"
                      v-if="dialogStatus === 'create'" required>
          <el-input type="password"
                    prefix-icon="el-icon-edit"
                    auto-complete="off"
                    :readonly="readonly"
                    v-model="tmpUser.password"/>
        </el-form-item>
        <el-form-item label="Role" required>
          <el-select placeholder="please select"
                     v-model="tmpUser.roleId">
            <el-option v-for="item in allRole"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">cancel</el-button>
        <el-button type="danger"
                   v-if="dialogStatus === 'create'"
                   @click="resetForm('tmpUser')">reset
        </el-button>
        <el-button type="success"
                   v-if="dialogStatus === 'create'"
                   :loading="btnLoading"
                   @click.native.prevent="createUser">create
        </el-button>
        <el-button type="primary"
                   v-else
                   :loading="btnLoading"
                   @click.native.prevent="updateUserRole">update
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { list as getUserList, register, remove } from '@/api/user'
  import { list as getRoleList, updateUserRole } from '@/api/role'
  import { isValidateEmail } from '@/utils/validate'
  import { unix2CurrentTime } from '@/utils'
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
          callback(new Error('username must be 3 or more characters'))
        } else {
          callback()
        }
      }
      const validatePassword = (rule, value, callback) => {
        if (value.length < 6) {
          callback(new Error('password must be 6 or more characters'))
        } else {
          callback()
        }
      }
      return {
        userList: [], // 用户列表
        allRole: [], // 全部角色
        filterRoleNameList: [], // 用于过滤表格角色的列表 http://element-cn.eleme.io/#/zh-CN/component/table#shai-xuan
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
        'userId'
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
          for (let i = 0; i < this.userList.length; i++) {
            const filterObject = {}
            filterObject.text = this.userList[i].roleName.split('_')[1] // ROLE_ABC
            filterObject.value = this.userList[i].roleName
            this.filterRoleNameList.push(filterObject)
          }
          this.listLoading = false
        })
      },
      unix2CurrentTime(timestamp) {
        return unix2CurrentTime(timestamp)
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
        // 表格序号 http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
        return (this.listQuery.page - 1) * this.listQuery.size + index + 1
      },
      filterRoleName(value, row) {
        return row.roleName === value
      },
      resetForm(formName) {
        this.$refs[formName].resetFields()
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
          if (valid && this.isUniqueInfo(this.tmpUser)) {
            this.btnLoading = true
            register(this.tmpUser).then(() => {
              this.$message.success('add success')
              this.getUserList()
              this.dialogFormVisible = false
              this.btnLoading = false
            })
          } else {
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
        updateUserRole(this.tmpUser).then(() => {
          this.$message.success('update success')
          this.getUserList()
          this.dialogFormVisible = false
        })
      },
      isUniqueInfo(user) {
        for (let i = 0; i < this.userList.length; i++) {
          if (this.userList[i].username === user.username) {
            this.$message.error('username already existed')
            return false
          }
          if (this.userList[i].email === user.email) {
            this.$message.error('email already existed')
            return false
          }
        }
        return true
      },
      removeUser(index) {
        this.$confirm('delete this user?', 'tip', {
          confirmButtonText: 'yes',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          const id = this.userList[index].id
          remove(id).then(() => {
            this.$message.success('delete success')
            this.getUserList()
          })
        })
      }
    }
  }
</script>
