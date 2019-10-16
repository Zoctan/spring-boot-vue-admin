<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('account:list')"
            @click.native.prevent="getAccountList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('account:add')"
            @click.native.prevent="showAddAccountDialog"
          >添加账号</el-button>
        </el-form-item>

        <span v-if="hasPermission('account:search')">
          <el-form-item>
            <el-input v-model="search.accountName" placeholder="账户名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="search.roleName" placeholder="角色">
              <el-option label="请选择" value />
              <div v-for="(role, index) in roleList" :key="index">
                <el-option :label="role.name" :value="role.name"/>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="accountList"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="#" align="center" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column label="账户名" align="center" prop="name" width="180" />
      <el-table-column label="邮箱" align="center" prop="email" width="200" />
      <el-table-column label="注册时间" align="center" prop="registerTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.registerTime) }}</template>
      </el-table-column>
      <el-table-column label="最后登录时间" align="center" prop="loginTime" width="160">
        <template slot-scope="scope">{{ scope.row.loginTime ? unix2CurrentTime(scope.row.loginTime) : '从未登录' }}</template>
      </el-table-column>
      <el-table-column label="角色" align="center" prop="roleName" width="120" />
      <el-table-column label="管理" align="center"
        v-if="hasPermission('role:update') || hasPermission('account:update') || hasPermission('account:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('role:update') && scope.row.id !== accountId"
            @click.native.prevent="showUpdateAccountDialog(scope.$index)"
          >账户</el-button>
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('account:update') && scope.row.id !== accountId"
            @click.native.prevent="showUpdateAccountRoleDialog(scope.$index)"
          >角色</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('account:delete') && scope.row.id !== accountId"
            @click.native.prevent="removeAccount(scope.$index)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.page"
      :page-size="listQuery.size"
      :total="total"
      :page-sizes="[9, 18, 36, 72]"
      layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="75px"
        style="width: 300px; margin-left:50px;"
        :model="tmpAccount"
        :rules="createRules"
        ref="tmpAccount"
      >
        <el-form-item label="账户名" prop="name" required>
          <el-input
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            :disabled="dialogStatus === 'updateRole'"
            v-model="tmpAccount.name"
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            :disabled="dialogStatus === 'updateRole'"
            v-model="tmpAccount.email"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password" required
        v-if="dialogStatus !== 'updateRole'">
          <el-input
            type="password"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpAccount.password"
            v-if="dialogStatus !== 'updateRole'"
          />
        </el-form-item>
        <el-form-item label="角色"
          v-if="dialogStatus === 'updateRole'">
          <el-select placeholder="请选择" v-model="tmpAccount.roleId">
            <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpAccount'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addAccount"
        >添加</el-button>
        <el-button
          type="primary"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateAccount"
        >更新资料</el-button>
        <el-button
          type="primary"
          v-if="dialogStatus === 'updateRole'"
          :loading="btnLoading"
          @click.native.prevent="updateAccountRole"
        >更新角色</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { list as getAccountList, search, register, remove, update as updateAccount } from '@/api/account'
import { list as getRoleList, updateAccountRole } from '@/api/role'
import { isValidateEmail } from '@/utils/validate'
import { unix2CurrentTime } from '@/utils'
import { mapGetters } from 'vuex'

export default {
  created() {
    if (this.hasPermission('role:update')) {
      this.getRoleList()
    }
    if (this.hasPermission('account:list')) {
      this.getAccountList()
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
    const validateName = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('账户名长度必须 ≥ 3'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码长度必须 ≥ 6'))
      } else {
        callback()
      }
    }
    return {
      accountList: [], // 用户列表
      roleList: [], // 全部角色
      filterRoleNameList: [], // 用于过滤表格角色的列表 http://element-cn.eleme.io/#/zh-CN/component/table#shai-xuan
      listLoading: false, // 数据加载等待动画
      total: 0, // 数据总数
      listQuery: {
        page: 1, // 页码
        size: 9 // 每页数量
      },
      dialogStatus: 'add',
      dialogFormVisible: false,
      textMap: {
        updateRole: '修改账号角色',
        update: '修改账号',
        add: '添加账号'
      },
      btnLoading: false, // 按钮等待动画
      tmpAccount: {
        accountId: '',
        email: '',
        name: '',
        password: '',
        roleId: 2 // 对应后端数据库普通用户角色Id
      },
      search: {
        page: null,
        size: null,
        accountName: null,
        roleName: null
      },
      createRules: {
        email: [{ required: true, trigger: 'blur', validator: validateEmail }],
        name: [{ required: true, trigger: 'blur', validator: validateName }],
        password: [
          { required: true, trigger: 'blur', validator: validatePassword }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['accountId'])
  },
  methods: {
    unix2CurrentTime,
    /**
     * 获取所有角色
     */
    getRoleList() {
      getRoleList().then(response => {
        this.roleList = response.data.list
      }).catch(res => {
        this.$message.error('加载角色失败')
      })
    },
    /**
     * 获取用户列表
     */
    getAccountList() {
      this.listLoading = true
      getAccountList(this.listQuery).then(response => {
        this.accountList = response.data.list
        this.total = response.data.total
        for (let i = 0; i < this.accountList.length; i++) {
          const filterObject = {}
          filterObject.text = this.accountList[i].roleName
          filterObject.value = this.accountList[i].roleName
          this.filterRoleNameList.push(filterObject)
        }
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载账户列表失败')
      })
    },
    searchBy() {
      this.btnLoading = true
      this.listLoading = true
      this.search.page = this.listQuery.page
      this.search.size = this.listQuery.size
      search(this.search).then(response => {
        this.accountList = response.data.list
        this.total = response.data.total
        this.listLoading = false
        this.btnLoading = false
      }).catch(res => {
        this.$message.error('搜索失败')
      })
    },
    /**
     * 改变每页数量
     * @param size 页大小
     */
    handleSizeChange(size) {
      this.listQuery.size = size
      this.listQuery.page = 1
      this.getAccountList()
    },
    /**
     * 改变页码
     * @param page 页号
     */
    handleCurrentChange(page) {
      this.listQuery.page = page
      this.getAccountList()
    },
    /**
     * 表格序号
     * 可参考自定义表格序号
     * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
     * @param index 数据下标
     * @returns 表格序号
     */
    getIndex(index) {
      return (this.listQuery.page - 1) * this.listQuery.size + index + 1
    },
    /**
     * 显示添加用户对话框
     */
    showAddAccountDialog() {
      // 显示新增对话框
      this.dialogFormVisible = true
      this.dialogStatus = 'add'
      this.tmpAccount.email = ''
      this.tmpAccount.name = ''
      this.tmpAccount.password = ''
    },
    /**
     * 添加用户
     */
    addAccount() {
      this.$refs.tmpAccount.validate(valid => {
        if (valid && this.isUniqueDetail(this.tmpAccount)) {
          this.btnLoading = true
          register(this.tmpAccount).then(() => {
            this.$message.success('添加成功')
            this.getAccountList()
            this.dialogFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('添加账户失败')
            this.btnLoading = false
          })
        }
      })
    },
    /**
     * 显示修改用户对话框
     * @param index 用户下标
     */
    showUpdateAccountDialog(index) {
      this.dialogFormVisible = true
      this.dialogStatus = 'update'
      this.tmpAccount.accountId = this.accountList[index].id
      this.tmpAccount.email = this.accountList[index].email
      this.tmpAccount.name = this.accountList[index].name
      this.tmpAccount.password = ''
      this.tmpAccount.roleId = this.accountList[index].roleId
    },
    /**
     * 更新用户
     */
    updateAccount() {
      updateAccount(this.tmpAccount).then(() => {
        this.$message.success('更新成功')
        this.getAccountList()
        this.dialogFormVisible = false
      }).catch(res => {
        this.$message.error('更新失败')
      })
    },
    /**
     * 显示修改用户角色对话框
     * @param index 用户下标
     */
    showUpdateAccountRoleDialog(index) {
      this.dialogFormVisible = true
      this.dialogStatus = 'updateRole'
      this.tmpAccount.accountId = this.accountList[index].id
      this.tmpAccount.email = this.accountList[index].email
      this.tmpAccount.name = this.accountList[index].name
      this.tmpAccount.password = ''
      this.tmpAccount.roleId = this.accountList[index].roleId
    },
    /**
     * 更新用户角色
     */
    updateAccountRole() {
      updateAccountRole(this.tmpAccount).then(() => {
        this.$message.success('更新成功')
        this.getAccountList()
        this.dialogFormVisible = false
      }).catch(res => {
        this.$message.error('更新失败')
      })
    },
    /**
     * 用户资料是否唯一
     * @param account 用户
     */
    isUniqueDetail(account) {
      for (let i = 0; i < this.accountList.length; i++) {
        if (this.accountList[i].name === account.name) {
          this.$message.error('账户名已存在')
          return false
        }
        if (this.accountList[i].email === account.email) {
          this.$message.error('邮箱已存在')
          return false
        }
      }
      return true
    },
    /**
     * 删除用户
     * @param index 用户下标
     */
    removeAccount(index) {
      this.$confirm('删除该账户？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const id = this.accountList[index].id
        remove(id).then(() => {
          this.$message.success('删除成功')
          this.getAccountList()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }
  }
}
</script>
