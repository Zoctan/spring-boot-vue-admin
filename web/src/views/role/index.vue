<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="success"
                     icon="el-icon-refresh"
                     v-if="hasPermission('role:list')"
                     @click.native.prevent="getRoleList">refresh
          </el-button>
          <el-button type="primary"
                     icon="el-icon-plus"
                     v-if="hasPermission('role:add')"
                     @click.native.prevent="showCreate">add
          </el-button>
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
                       prop="name"/>
      <el-table-column label="Permission"
                       align="center">
        <template slot-scope="scope">
          <el-tag type="success"
                  v-if="scope.row.name === 'ROLE_ADMIN'">all
          </el-tag>
          <el-tag type="success"
                  v-else-if="scope.row.resourceList.length === 0">none
          </el-tag>
          <div v-else
               v-for="item in scope.row.resourceList">
            <span style="margin-right: 3px;">{{ item.resource }}</span>
            <span v-for="item2 in item.handleList"
                  style="margin-right: 3px;">
              <el-tag type="success"
                      v-text="item2.handle"/>
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Admin"
                       align="center"
                       v-if="hasPermission('role:update') || hasPermission('role:delete')">
        <template slot-scope="scope">
          <el-button type="primary"
                     icon="el-icon-edit-outline"
                     v-if="hasPermission('role:update') && scope.row.name !== 'ROLE_ADMIN'"
                     @click="showUpdate(scope.$index)">update
          </el-button>
          <el-button type="danger"
                     icon="el-icon-delete"
                     v-if="hasPermission('role:delete') && scope.row.name !== 'ROLE_ADMIN'"
                     @click="removeRole(scope.$index)">delete
          </el-button>
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
                    v-model="tempRole.name">
            <template slot="prepend">ROLE_</template>
          </el-input>
        </el-form-item>
        <el-form-item label="Permission" required>
          <div v-for="(permission, index) in allPermission">
            <el-button size="mini"
                       :type="isMenuNone(index) ? '' : (isMenuAll(index) ? 'success' : 'primary')"
                       @click="checkAll(index)">{{ permission.resource }}
            </el-button>
            <el-checkbox-group v-model="tempRole.permissionIdList">
              <el-checkbox v-for="item in permission.handleList"
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
                   @click.native.prevent="createRole">create
        </el-button>
        <el-button type="primary"
                   v-else :loading="btnLoading"
                   @click.native.prevent="updateRole">update
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { list as getRoleList, listResourcePermission, add as addRole, update as updateRole, remove } from '@/api/role'
  import { isValidateRoleName } from '@/utils/validate'
  import { mapGetters } from 'vuex'

  export default {
    created() {
      if (this.hasPermission('role:update')) {
        this.getAllPermission()
      }
      if (this.hasPermission('role:list')) {
        this.getRoleList()
      }
    },
    data() {
      const validateRoleName = (rule, value, callback) => {
        const roleName = 'ROLE_' + value
        if (!isValidateRoleName(roleName)) {
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
        this.tempRole.name = ''
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
        for (let i = 0; i < role.resourceList.length; i++) {
          const handleList = role.resourceList[i].handleList
          for (let j = 0; j < handleList.length; j++) {
            const handle = handleList[j]
            this.tempRole.permissionIdList.push(handle.id)
          }
        }
      },
      createRole() {
        // 添加新角色
        this.$refs.tempRole.validate(valid => {
          if (valid && this.isRoleNameUnique(this.tempRole.id, this.tempRole.name)) {
            this.btnLoading = true
            addRole(this.tempRole).then(() => {
              this.$message.success('add success')
              this.getRoleList()
              this.dialogFormVisible = false
              this.btnLoading = false
            })
          } else {
            console.log('form not validate')
            return false
          }
        })
      },
      updateRole() {
        // 修改角色
        this.$refs.tempRole.validate(valid => {
          if (valid && this.isRoleNameUnique(this.tempRole.id, this.tempRole.name)) {
            this.btnLoading = true
            updateRole(this.tempRole).then(() => {
              this.$message.success('update success')
              this.getRoleList()
              this.dialogFormVisible = false
              this.btnLoading = false
            })
          } else {
            console.log('form not validate')
            return false
          }
        })
      },
      isRoleNameUnique(id, name) {
        // 校验名称重复
        for (let i = 0; i < this.roleList.length; i++) {
          if (this.roleList[i].id !== id && this.roleList[i].name === name) {
            this.$message.error('role name already existed')
            return false
          }
        }
        return true
      },
      removeRole(index) {
        this.$confirm('delete this role?', 'tip', {
          confirmButtonText: 'yes',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          const roleId = this.roleList[index].id
          remove(roleId).then(() => {
            this.$message.success('delete success')
            this.getRoleList()
          })
        })
      },
      isMenuNone(index) {
        // 判断本级菜单内的权限是否一个都没选
        const handleList = this.allPermission[index].handleList
        for (let i = 0; i < handleList.length; i++) {
          if (this.tempRole.permissionIdList.indexOf(handleList[i].id) > -1) {
            return false
          }
        }
        return true
      },
      isMenuAll(index) {
        // 判断本级菜单内的权限是否全选了
        const handleList = this.allPermission[index].handleList
        for (let i = 0; i < handleList.length; i++) {
          if (this.tempRole.permissionIdList.indexOf(handleList[i].id) < 0) {
            return false
          }
        }
        return true
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
        const handleList = this.allPermission[index].handleList
        for (let i = 0; i < handleList.length; i++) {
          this.addUnique(handleList[i].id, this.tempRole.permissionIdList)
        }
      },
      cancelAll(index) {
        // 全部取消选中
        const handleList = this.allPermission[index].handleList
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
        const handleList = this.allPermission[index].handleList
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
