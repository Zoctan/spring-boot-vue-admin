<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPermission('role:add')" @click.native.prevent="showCreate">add</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="roleList" v-loading.body="listLoading" element-loading-text="loading" border fit highlight-current-row>
      <el-table-column align="center" label="Index">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="Role Name" prop="name" />
      <el-table-column align="center" label="Permission">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.name === 'ROLE_ADMIN'">all</el-tag>
          <el-tag type="success" v-else-if="scope.row.permissionList.length === 0">none</el-tag>
          <span v-else v-for="permission in scope.row.permissionList" style="margin-right: 3px;">
          <el-tag type="success" v-text="permission.code" />
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="Admin" v-if="hasPermission('role:update') || hasPermission('role:delete')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" v-if="hasPermission('role:update')" @click="showUpdate(scope.$index)">update</el-button>
          <el-button type="danger" icon="delete" v-if="roleName === 'ROLE_ADMIN' && scope.row.name !== 'ROLE_ADMIN'" @click="removeRole(scope.$index)">delete</el-button>
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
      <el-form class="small-space" :model="tempRole" label-position="left" label-width="100px" style='width: 500px; margin-left:50px;'>
        <el-form-item label="Role name" required>
          <el-input type="text" v-model="tempRole.name" />
        </el-form-item>
        <el-form-item label="Permission" required>
          <!--
          <div v-for=" (id, name) in allPermission">
            <span style="width: 100px;display: inline-block;">
              <el-button :type="isMenuAll(id)?'success':'primary'" size="mini"
                         style="width:80px;"
                         @click="checkAll(id)">{{name}}</el-button>
            </span>
            <div style="display: inline-block;margin-left:20px;">
              <el-checkbox-group v-model="tempRole.permissionList">
                <el-checkbox v-for="perm in tempRole.permissionList" :label="perm.id"
                             :key="perm.id">
                  <span :class="{requiredPerm:perm.requiredPerm===1}">{{perm.permissionName}}</span>
                </el-checkbox>
              </el-checkbox-group>
            </div>
          </div>
          -->
            <div v-for="permission in allPermission">
              <div>{{permission.resource}}</div>
              <div v-for="handle in permission.handleList">
                <div>{{handle}}</div>
              </div>
            </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">cancel</el-button>
        <el-button type="success" v-if="dialogStatus==='create'" :loading="btnLoading" @click.native.prevent="createRole">create</el-button>
        <el-button type="primary" v-else :loading="btnLoading" @click.native.prevent="updateRole">update</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { list as getRoleList, add as addRole } from '@/api/role'
import { list as getPermissionList } from '@/api/permission'
import { mapGetters } from 'vuex'
export default {
  created() {
    if (this.hasPermission('role:list')) {
      this.getAllPermission()
      this.getRoleList()
    }
  },
  data() {
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
      getPermissionList().then(response => {
        for (let i = 0; i < response.data.list.length; i++) {
          const permission = {
            'resource': null,
            'handleList': []
          }
          permission.resource = response.data.list[i].resource
          for (let i = 0; i < response.data.list.length; i++) {
            if (response.data.list[i].resource === permission.resource) {
              permission.handleList.push(response.data.list[i].handle)
            }
          }
          this.allPermission.push(permission)
        }
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
      for (let i = 0; i < role.menus.length; i++) {
        const perm = role.menus[i].permissionList
        for (let j = 0; j < perm.length; j++) {
          this.tempRole.permissionIdList.push(perm[j].permissionId)
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
      if (this.tempRole.permissionList.length === 0) {
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
      for (let j = 0; j < roles.length; j++) {
        if (roles[j].roleName === roleName && (!roleId || roles[j].roleId !== roleId)) {
          this.$message.error('角色名称已存在')
          result = false
          break
        }
      }
      return result
    },
    removeRole($index) {
      const _vue = this
      this.$confirm('确定删除此角色?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        const role = _vue.list[$index]
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
    isMenuAll(id) {
      // 判断本级菜单内的权限是否全选了
      const menu = this.allPermission[id].permissions
      let result = true
      for (let j = 0; j < menu.length; j++) {
        if (this.tempRole.permissions.indexOf(menu[j].id) < 0) {
          result = false
          break
        }
      }
      return result
    },
    checkAll(id) {
      // 点击菜单   相当于全选按钮
      const v = this
      if (v.isMenuAll(id)) {
        // 如果已经全选了,则全部取消
        v.noPerm(id)
      } else {
        // 如果尚未全选,则全选
        v.allPerm(id)
      }
    },
    allPerm(id) {
      // 全部选中
      const menu = this.allPermission[id].permissionList
      for (let j = 0; j < menu.length; j++) {
        this.addUnique(menu[j].id, this.tempRole.permissionList)
      }
    },
    noPerm(id) {
      // 全部取消选中
      const menu = this.allPermission[id].permissionList
      for (let j = 0; j < menu.length; j++) {
        const idIndex = this.tempRole.permissionList.indexOf(menu[j].id)
        if (idIndex > -1) {
          this.tempRole.permissionList.splice(idIndex, 1)
        }
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
