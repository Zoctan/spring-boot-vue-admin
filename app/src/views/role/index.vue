<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="success" icon="el-icon-refresh"
                     v-if="hasPermission('role:list')"
                     @click.native.prevent="getRoleList">refresh
          </el-button>
          <el-button type="primary" icon="el-icon-plus"
                     v-if="hasPermission('role:add')"
                     @click.native.prevent="showAddRoleDialog">add
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="roleList"
              v-loading.body="listLoading"
              element-loading-text="loading"
              border fit highlight-current-row>
      <el-table-column label="#" align="center" width="80">
        <template slot-scope="scope">
          <span v-text="getTableIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column label="Role Name" align="center" prop="name"/>
      <el-table-column label="Permission" align="center">
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
            <span style="margin-right: 3px;"
                  v-for="item2 in item.handleList">
              <el-tag type="success"
                      v-text="item2.handle"/>
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Admin" align="center"
                       v-if="hasPermission('role:update') || hasPermission('role:delete')">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit-outline"
                     v-if="hasPermission('role:update') && scope.row.name !== 'ROLE_ADMIN'"
                     @click="showUpdateRoleDialog(scope.$index)">update
          </el-button>
          <el-button type="danger" icon="el-icon-delete"
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
      <el-form status-icon class="small-space" label-position="left" label-width="100px" style="width: 500px; margin-left:50px;"
               :model="tempRole"
               :rules="createRules"
               ref="tempRole">
        <el-form-item label="Role name" prop="name" required>
          <el-input type="text" prefix-icon="el-icon-edit" auto-complete="off"
                    v-model="tempRole.name">
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
                   v-if="dialogStatus === 'add'"
                   :loading="btnLoading"
                   @click.native.prevent="addRole">add
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
      /**
       * 验证角色名
       * @param rule 规则
       * @param value 角色名
       * @param callback 回调
       */
      const validateRoleName = (rule, value, callback) => {
        if (!isValidateRoleName(value)) {
          callback(new Error('role name format error. eg. ROLE_ABC'))
        } else {
          callback()
        }
      }
      return {
        roleList: [],
        allPermission: [],
        listLoading: false,
        total: 0,
        listQuery: {
          page: 1,
          size: 30
        },
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          update: 'Update Role Permission',
          add: 'Add Role'
        },
        btnLoading: false,
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
      /**
       * 获取所有角色权限
       */
      getAllPermission() {
        listResourcePermission().then(response => {
          this.allPermission = response.data.list
        })
      },
      /**
       * 获取角色列表
       */
      getRoleList() {
        this.listLoading = true
        getRoleList(this.listQuery).then(response => {
          this.roleList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        })
      },
      /**
       * 改变每页数量
       * @param size 页大小
       */
      handleSizeChange(size) {
        this.listQuery.page = 1
        this.listQuery.size = size
        this.getRoleList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.listQuery.page = page
        this.getRoleList()
      },
      /**
       * 表格序号
       * @param index 数据下标
       * @returns 表格序号
       */
      getTableIndex(index) {
        return (this.listQuery.page - 1) * this.listQuery.size + index + 1
      },
      /**
       * 显示新增角色对话框
       */
      showAddRoleDialog() {
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tempRole.name = ''
        this.tempRole.id = ''
        this.tempRole.permissionIdList = []
      },
      /**
       * 显示更新角色的对话框
       * @param index 角色下标
       */
      showUpdateRoleDialog(index) {
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
      /**
       * 添加新角色
       */
      addRole() {
        this.$refs.tempRole.validate(valid => {
          if (valid && this.isRoleNameUnique(this.tempRole.id, this.tempRole.name)) {
            this.btnLoading = true
            addRole(this.tempRole).then(() => {
              this.$message.success('Add success')
              this.getRoleList()
              this.dialogFormVisible = false
              this.btnLoading = false
            })
          } else {
            console.log('form not validate')
          }
        })
      },
      /**
       * 修改角色
       */
      updateRole() {
        this.$refs.tempRole.validate(valid => {
          if (valid && this.isRoleNameUnique(this.tempRole.id, this.tempRole.name)) {
            this.btnLoading = true
            updateRole(this.tempRole).then(() => {
              this.$message.success('Update success')
              this.getRoleList()
              this.dialogFormVisible = false
              this.btnLoading = false
            })
          } else {
            console.log('form not validate')
          }
        })
      },
      /**
       * 校验角色名是否已经存在
       * @param id 角色id
       * @param name 角色名
       * @returns {boolean}
       */
      isRoleNameUnique(id, name) {
        for (let i = 0; i < this.roleList.length; i++) {
          if (this.roleList[i].id !== id && this.roleList[i].name === name) {
            this.$message.error('Role name already existed')
            return false
          }
        }
        return true
      },
      /**
       * 移除角色
       * @param index 角色下标
       * @returns {boolean}
       */
      removeRole(index) {
        this.$confirm('Delete this role?', 'Waring', {
          confirmButtonText: 'yes',
          cancelButtonText: 'no',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          const roleId = this.roleList[index].id
          remove(roleId).then(() => {
            this.$message.success('Delete success!')
            this.getRoleList()
          })
        })
      },
      /**
       * 判断角色菜单内的权限是否一个都没选
       * @param index 下标
       * @returns {boolean}
       */
      isMenuNone(index) {
        const handleList = this.allPermission[index].handleList
        for (let i = 0; i < handleList.length; i++) {
          if (this.tempRole.permissionIdList.indexOf(handleList[i].id) > -1) {
            return false
          }
        }
        return true
      },
      /**
       * 判断角色菜单内的权限是否全选了
       * @param index 下标
       * @returns {boolean}
       */
      isMenuAll(index) {
        const handleList = this.allPermission[index].handleList
        for (let i = 0; i < handleList.length; i++) {
          if (this.tempRole.permissionIdList.indexOf(handleList[i].id) < 0) {
            return false
          }
        }
        return true
      },
      /**
       * 根据菜单状态check所有checkbox
       * @param index 下标
       */
      checkAll(index) {
        if (this.isMenuAll(index)) {
          // 如果已经全选了,则全部取消
          this.cancelAll(index)
        } else {
          // 如果尚未全选,则全选
          this.selectAll(index)
        }
      },
      /**
       * checkbox全部选中
       * @param index 下标
       */
      selectAll(index) {
        const handleList = this.allPermission[index].handleList
        for (let i = 0; i < handleList.length; i++) {
          this.addUnique(handleList[i].id, this.tempRole.permissionIdList)
        }
      },
      /**
       * checkbox全部取消选中
       * @param index 下标
       */
      cancelAll(index) {
        const handleList = this.allPermission[index].handleList
        for (let i = 0; i < handleList.length; i++) {
          const idIndex = this.tempRole.permissionIdList.indexOf(handleList[i].id)
          if (idIndex > -1) {
            this.tempRole.permissionIdList.splice(idIndex, 1)
          }
        }
      },
      /**
       * 本方法会在勾选状态改变之后触发
       * @param item 选项
       * @param index 对应下标
       */
      handleChecked(item, index) {
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
      /**
       * 将角色菜单必选的权限勾上（这里并没有做必选的数据库字段）
       * @param index 权限对应下标
       */
      makePermissionChecked(index) {
        const handleList = this.allPermission[index].handleList
        for (let i = 0; i < handleList.length; i++) {
          this.addUnique(handleList[i].id, this.tempRole.permissionIdList)
        }
      },
      /**
       * 数组内防重复地添加元素
       * @param val 值
       * @param arr 数组
       */
      addUnique(val, arr) {
        const _index = arr.indexOf(val)
        if (_index < 0) {
          arr.push(val)
        }
      }
    }
  }
</script>
