<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary"
                     icon="el-icon-plus"
                     v-if="hasPermission('role:add')"
                     @click.native.prevent="showCreate">add</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="roleList"
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
      <el-table-column label="Role Name"
                       align="center"
                       prop="name" />
      <el-table-column label="Permission"
                       align="center">
        <template slot-scope="scope">
          <el-tag type="success"
                  v-if="scope.row.name === 'ROLE_ADMIN'">all</el-tag>
          <el-tag type="success"
                  v-else-if="scope.row.permissionList.length === 0">none</el-tag>
          <span v-else
                v-for="permission in scope.row.permissionList"
                style="margin-right: 3px;">
          <el-tag type="success"
                  v-text="permission.code" />
          </span>
        </template>
      </el-table-column>
      <el-table-column label="Admin"
                       align="center"
                       v-if="hasPermission('role:update') || hasPermission('role:delete')">
        <template slot-scope="scope">
          <el-button type="primary"
                     icon="el-icon-edit-outline"
                     v-if="hasPermission('role:update')"
                     @click="showUpdate(scope.$index)">update</el-button>
          <el-button type="danger"
                     icon="el-icon-delete"
                     v-if="roleName === 'ROLE_ADMIN' && scope.row.name !== 'ROLE_ADMIN'"
                     @click="removeRole(scope.$index)">delete</el-button>
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
               :model="tempRole"
               :rules="createRules"
               ref="tempRole"
               status-icon
               label-position="left"
               label-width="100px"
               style="width: 500px; margin-left:50px;">
        <el-form-item label="Role name"
                      prop="name"
                      required>
          <el-input type="text"
                    prefix-icon="el-icon-edit"
                    auto-complete="off"
                    v-model="tempRole.name" />
        </el-form-item>
        <el-form-item label="Permission" required>
            <div v-for="(permission, index) in allPermission">
              <el-button size="mini"
                         :type="isMenuNone(index) ? '' : (isMenuAll(index) ? 'success' : 'primary')"
                         @click="checkAll(index)">{{ permission.resource }}</el-button>
              <el-checkbox-group v-model="tempRole.permissionIdList">
                <el-checkbox v-for="item in permission.resourceHandleList"
                             :key="item.id"
                             :label="item.id"
                             @change="handleChecked(item, _index)">
                  <span>{{ item.handle }}</span>
                </el-checkbox>
              </el-checkbox-group>
            </div>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">cancel</el-button>
        <el-button type="success"
                   v-if="dialogStatus === 'create'"
                   :loading="btnLoading"
                   @click.native.prevent="createRole">create</el-button>
        <el-button type="primary"
                   v-else :loading="btnLoading"
                   @click.native.prevent="updateRole">update</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { list as getRoleList, listResourcePermission, add as addRole } from '@/api/role'
  import { isValidateRoleName } from '@/utils/validate'
  import { mapGetters } from 'vuex'

  export default {
    created() {
      if (this.hasPermission('role:list')) {
        this.getAllPermission()
        this.getRoleList()
      }
    },
    data() {
      const validateRoleName = (rule, value, callback) => {
        if (!isValidateRoleName(value)) {
          callback(new Error('role name format error. eg. ROLE_ABC'))
        } else {
          callback()
        }
      }
      return {
        roleList: [], // 角色列表
        allPermission: [], // 全部权限
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        listQuery: {
          page: 1, // 页码
          size: 30 // 每页数量
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: 'Update Role Permission',
          create: 'Create Role'
        },
        btnLoading: false, // 按钮等待动画
        tempRole: {
          id: '',
          name: '',
          permissionIdList: []
        },
        createRules: {
          name: [{ required: true, trigger: 'blur', validator: validateRoleName }]
        }
      }
    },
    computed: {
      ...mapGetters([
        'roleName'
      ])
    },
    methods: {
      getAllPermission() {
        listResourcePermission().then(response => {
          this.allPermission = response.data.list
        })
      },
      getRoleList() {
        // 查询列表
        this.listLoading = true
        getRoleList(this.listQuery).then(response => {
          this.roleList = response.data.list
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
        this.getRoleList()
      },
      handleFilter() {
        // 查询事件
        this.listQuery.page = 1
        this.getRoleList()
      },
      getIndex(index) {
        // 表格序号
        return (this.listQuery.page - 1) * this.listQuery.size + index + 1
      },
      showCreate() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'create'
        this.tempRole.name = 'ROLE_'
        this.tempRole.id = ''
        this.tempRole.permissionIdList = []
      },
      showUpdate(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        const role = this.roleList[index]
        this.tempRole.name = role.name
        this.tempRole.id = role.id
        this.tempRole.permissionIdList = []
        for (let i = 0; i < role.menus.length; i++) {
          const perm = role.menus[i].permissionList
          for (let i = 0; i < perm.length; i++) {
            this.tempRole.permissionIdList.push(perm[i].permissionId)
          }
        }
      },
      createRole() {
        if (!this.checkRoleNameUnique()) {
          return
        }
        if (!this.checkPermissionNum()) {
          return
        }
        // 添加新角色
        addRole().then(response => {
          this.getList()
          this.dialogFormVisible = false
        })
      },
      updateRole() {
        if (!this.checkRoleNameUnique(this.tempRole.roleId)) {
          return
        }
        if (!this.checkPermissionNum()) {
          return
        }
        // 修改角色
        this.request({
          url: '/user/updateRole',
          method: 'post',
          data: this.tempRole
        }).then(() => {
          this.getList()
          this.dialogFormVisible = false
        })
      },
      checkPermissionNum() {
        // 校验至少有一种权限
        if (this.tempRole.permissionIdList.length === 0) {
          this.$message.error('请至少选择一种权限')
          return false
        }
        return true
      },
      checkRoleNameUnique(roleId) {
        // 校验名称重复
        const roleName = this.tempRole.roleName
        if (!roleName) {
          this.$message.error('请填写角色名称')
          return false
        }
        const roles = this.list
        let result = true
        for (let i = 0; i < roles.length; i++) {
          if (roles[i].roleName === roleName && (!roleId || roles[i].roleId !== roleId)) {
            this.$message.error('角色名称已存在')
            result = false
            break
          }
        }
        return result
      },
      removeRole(index) {
        const _vue = this
        this.$confirm('确定删除此角色?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          const role = _vue.list[index]
          _vue.request({
            url: '/user/deleteRole',
            method: 'post',
            data: {
              roleId: role.roleId
            }
          }).then(() => {
            _vue.getList()
          }).catch(e => {
          })
        })
      },
      isMenuNone(index) {
        // 判断本级菜单内的权限是否一个都没选
        const handleList = this.allPermission[index].resourceHandleList
        let result = true
        for (let i = 0; i < handleList.length; i++) {
          if (this.tempRole.permissionIdList.indexOf(handleList[i].id) > -1) {
            result = false
            break
          }
        }
        return result
      },
      isMenuAll(index) {
        // 判断本级菜单内的权限是否全选了
        const handleList = this.allPermission[index].resourceHandleList
        let result = true
        for (let i = 0; i < handleList.length; i++) {
          if (this.tempRole.permissionIdList.indexOf(handleList[i].id) < 0) {
            result = false
            break
          }
        }
        return result
      },
      checkAll(index) {
        if (this.isMenuAll(index)) {
          // 如果已经全选了,则全部取消
          this.cancelAll(index)
        } else {
          // 如果尚未全选,则全选
          this.selectAll(index)
        }
      },
      selectAll(index) {
        // 全部选中
        const handleList = this.allPermission[index].resourceHandleList
        for (let i = 0; i < handleList.length; i++) {
          this.addUnique(handleList[i].id, this.tempRole.permissionIdList)
        }
      },
      cancelAll(index) {
        // 全部取消选中
        const handleList = this.allPermission[index].resourceHandleList
        for (let i = 0; i < handleList.length; i++) {
          const idIndex = this.tempRole.permissionIdList.indexOf(handleList[i].id)
          if (idIndex > -1) {
            this.tempRole.permissionIdList.splice(idIndex, 1)
          }
        }
      },
      handleChecked(item, index) {
        // 本方法会在勾选状态改变之后触发
        if (this.tempRole.permissionIdList.indexOf(item.id) > -1) {
          // 选中事件
          // 如果之前未勾选本权限,现在勾选完之后,tempRole里就会包含本id
          // 那么就要将必选的权限勾上
          this.makePermissionChecked(index)
        } else {
          // 取消选中事件
          this.cancelAll(index)
        }
      },
      makePermissionChecked(index) {
        // 将本菜单必选的权限勾上
        const handleList = this.allPermission[index].resourceHandleList
        for (let i = 0; i < handleList.length; i++) {
          this.addUnique(handleList[i].id, this.tempRole.permissionIdList)
        }
      },
      addUnique(val, arr) {
        // 数组内防重复地添加元素
        const _index = arr.indexOf(val)
        if (_index < 0) {
          arr.push(val)
        }
      }
    }
  }
</script>
<style scoped>
  .requiredPerm {
    color: #ff0e13;
  }
</style>
