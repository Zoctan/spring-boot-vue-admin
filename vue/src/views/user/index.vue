<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPermission('user:add')" @click.native.prevent="showCreate">添加</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="userList" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit highlight-current-row>
      <el-table-column align="center" label="序号">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户名" prop="username" />
      <el-table-column align="center" label="角色" >
        <template slot-scope="scope">
          <el-tag type="success" v-text="scope.row.roleNameZh" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" v-if="hasPermission('user:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" v-if="scope.row.username !== username" @click.native.prevent="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="delete" v-if="hasPermission('user:delete') && scope.row.username !== username" @click.native.prevent="removeUser(scope.$index)">删除</el-button>
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
        <el-form-item label="邮箱" prop="email" required>
          <el-input type="text" v-model="tmpUser.email" :readonly="readonly" />
        </el-form-item>
        <el-form-item label="用户名" prop="username" required>
          <el-input type="text" v-model="tmpUser.username" :readonly="readonly" />
        </el-form-item>
        <el-form-item label="密码" prop="password" required>
          <el-input type="password" v-model="tmpUser.password" :readonly="readonly" />
        </el-form-item>
        <el-form-item label="角色" v-if="dialogStatus === 'update'" required>
          <el-select v-model="tmpUser.roleId" placeholder="请选择">
            <el-option v-for="item in roleList"
                       :key="item.id"
                       :label="item.nameZh"
                       :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="success" v-if="dialogStatus === 'create'" :loading="btnLoading" @click.native.prevent="createUser">创建</el-button>
        <el-button type="primary" v-else :loading="btnLoading" @click.native.prevent="updateUser">修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { list as getUserList, register, update, remove } from '@/api/user'
import { list as getRoleList } from '@/api/role'
import { isValidateEmail } from '@/utils/validate'
import { mapGetters } from 'vuex'

export default {
  created() {
    this.getUserList()
    if (this.hasPermission('user:update')) {
      this.getRoleList()
    }
  },
  data() {
    const validateEmail = (rule, value, callback) => {
      if (!isValidateEmail(value)) {
        callback(new Error('邮箱格式错误'))
      } else {
        callback()
      }
    }
    const validateUsername = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('用户名不能小于3位'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    return {
      userList: [], // 用户列表
      roleList: [], // 角色列表
      listLoading: false, // 数据加载等待动画
      total: 0, // 数据总数
      listQuery: {
        page: 1, // 页码
        size: 30 // 每页数量
      },
      dialogStatus: 'create',
      dialogFormVisible: false,
      textMap: {
        update: '修改角色',
        create: '新建用户'
      },
      btnLoading: false, // 按钮等待动画
      readonly: false, // 只读输入框
      tmpUser: {
        id: '',
        email: '',
        username: '',
        password: '',
        roleId: ''
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
    getUserList() {
      this.listLoading = true
      getUserList(this.listQuery).then(response => {
        this.userList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    getRoleList() {
      getRoleList().then(response => {
        this.roleList = response.data.list
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
              this.$message.success('添加成功')
              this.getUserList()
              this.dialogFormVisible = false
            } else {
              this.$message.error(response.message)
            }
            this.btnLoading = false
          })
        } else {
          console.log('表单未通过验证')
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
    updateUser() {
      update(this.tmpUser).then(data => {
        if (data.status === 200) {
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            duration: 1000,
            onClose: () => {
              this.getUserList()
            }
          })
        } else {
          this.$message.error(data.message)
        }
      })
    },
    removeUser(index) {
      this.$confirm('确定删除此用户?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        const id = this.userList[index].id
        remove(id).then(response => {
          console.info(response)
          if (response.status === 200) {
            this.$message.success('删除成功')
            this.getUserList()
          } else {
            this.$message.error('删除失败')
          }
        })
      })
    }
  }
}
</script>
