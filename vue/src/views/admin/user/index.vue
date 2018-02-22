<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button size="small" type="primary" icon="plus" v-if="hasPermission('user:add')" @click="showCreate">添加</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="userList" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit highlight-current-row>
      <el-table-column align="center" label="序号">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户名" prop="username"></el-table-column>
      <el-table-column align="center" label="角色" >
        <template slot-scope="scope">
          <span v-for="item in roleList">
            <el-tag type="success" v-if="item.name === scope.row.roleName" v-text="item.nameZh"></el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" v-if="hasPermission('user:update')">
        <template slot-scope="scope">
          <el-button size="small" type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button size="small" type="danger" icon="delete" v-if="scope.row.id !== userId" @click="removeUser(scope.$index)">删除</el-button>
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
        <el-form-item v-if="dialogStatus === 'create'" label="用户名">
          <el-input type="text" v-model="tmpUser.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item v-if="dialogStatus === 'create'" label="密码">
          <el-input type="password" v-model="tmpUser.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="角色" required>
          <el-select v-model="tmpUser.roleId" placeholder="请选择">
            <el-option v-for="item in roleList"
              :key="item.id"
              :value="item.nameZh">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="success" v-if="dialogStatus === 'create'" :loading="loading" @click.native.prevent="createUser">创建</el-button>
        <el-button type="primary" v-else @click="updateUser">修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { list as userList, register, update, remove } from '@/api/user'
import { list as roleList } from '@/api/role'
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
      userList: [], // 表格的数据
      listLoading: false, // 数据加载等待动画
      total: 0, // 分页组件--数据总条数
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
      loading: false
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
      userList(this.listQuery).then(response => {
        this.userList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    getRoleList() {
      roleList().then(response => {
        this.roleList = response.data.list
      })
    },
    handleSizeChange(val) {
      // 改变每页数量
      this.listQuery.size = val
      this.handleFilter()
    },
    handleCurrentChange(val) {
      // 改变页码
      this.listQuery.page = val
      this.getUserList()
    },
    handleFilter() {
      // 查询事件
      this.listQuery.page = 1
      this.getUserList()
    },
    getIndex($index) {
      // 表格序号
      return (this.listQuery.page - 1) * this.listQuery.size + $index + 1
    },
    showCreate() {
      // 显示新增对话框
      this.tmpUser.username = ''
      this.tmpUser.password = ''
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createUser() {
      this.$refs.tmpUser.validate(valid => {
        if (valid) {
          this.loading = true
          register(this.tmpUser).then(response => {
            console.info(response.data)
            if (response.data.status === 200) {
              this.getUserList()
              this.loading = false
              this.dialogFormVisible = false
            } else {
              this.$message.error(response.data.message)
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    showUpdate($index) {
      this.tempUser = this.userList[$index]
      this.tempUser.deleteStatus = '1'
      this.tempUser.password = ''
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    updateUser() {
      update(this.tmpUser).then(data => {
        const _vue = this
        if (data.status === 200) {
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            duration: 1000,
            onClose: () => {
              _vue.getUserList()
            }
          })
        } else {
          this.$message.error(data.message)
        }
      })
    },
    removeUser($index) {
      const _vue = this
      this.$confirm('确定删除此用户?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        const user = _vue.list[$index]
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
