<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPermission('user:add')" @click="showCreate">添加</el-button>
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
          <span v-for="item in roleList">
            <el-tag type="success" v-if="item.name === scope.row.roleName" v-text="item.nameZh" />
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" v-if="hasPermission('user:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="delete" v-if="scope.row.id !== userId" @click="removeUser(scope.$index)">删除</el-button>
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
        <el-form-item label="用户名" prop="username" required>
          <el-input name="username" type="text" v-model="tmpUser.username" :readonly="readonly" />
        </el-form-item>
        <el-form-item label="密码" prop="password" required>
          <el-input name="password" type="password" v-model="tmpUser.password" :readonly="readonly" />
        </el-form-item>
        <el-form-item label="角色" required>
          <el-select v-model="tmpUser.roleId" placeholder="请选择">
            <el-option v-for="item in roleList"
                       :key="item.id"
                       :value="item.nameZh" />
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
import { mapGetters } from 'vuex'

export default {
  data() {
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
      listLoading: false, // 数据加载等待动画
      total: 0, // 数据总数
      listQuery: {
        page: 1, // 页码
        size: 30 // 每页条数
      },
      roleList: [], // 角色列表
      dialogStatus: 'create',
      dialogFormVisible: false,
      textMap: {
        update: '修改角色',
        create: '新建用户'
      },
      tmpUser: {
        username: '',
        password: '',
        roleId: ''
      },
      createRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      btnLoading: false,
      readonly: false
    }
  },
  created() {
    this.getUserList()
    if (this.hasPermission('user:add') || this.hasPermission('user:update')) {
      this.getRoleList()
    }
  },
  computed: {
    ...mapGetters([
      'userId'
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
      this.tmpUser.username = ''
      this.tmpUser.password = ''
      this.readonly = false
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createUser() {
      this.$refs.tmpUser.validate(valid => {
        if (valid) {
          this.btnLoading = true
          register(this.tmpUser).then(response => {
            console.info(response.data)
            if (response.data.status === 200) {
              this.getUserList()
              this.dialogFormVisible = false
            } else {
              this.$message.error(response.data.message)
            }
            this.btnLoading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    showUpdate(index) {
      // 显示修改对话框
      this.tempUser = this.userList[index]
      console.info(this.tempUser.username)
      this.tempUser.password = '******'
      this.readonly = true
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
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
      const _vue = this
      this.$confirm('确定删除此用户?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        const user = _vue.list[index]
        user.deleteStatus = '2'
        remove(user).then(data => {
          if (data.status === 200) {
            _vue.getUserList()
          } else {
            _vue.$message.error('删除失败')
          }
        })
      })
    }
  }
}
</script>
