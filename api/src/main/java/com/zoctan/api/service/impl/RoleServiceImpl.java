package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.dto.RoleWithPermission;
import com.zoctan.api.mapper.PermissionMapper;
import com.zoctan.api.mapper.RoleMapper;
import com.zoctan.api.mapper.RolePermissionMapper;
import com.zoctan.api.model.Role;
import com.zoctan.api.model.RolePermission;
import com.zoctan.api.model.RoleWithResource;
import com.zoctan.api.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<RoleWithResource> findRoleWithPermission() {
        // 由于mybatis在嵌套查询时和pagehelper有冲突
        // 暂时用for循环代替
        //return this.roleMapper.findRoleWithPermission();
        List<RoleWithResource> roles = roleMapper.findRoles();
        roles.forEach(role -> {
            List<com.zoctan.api.model.Resource> resources = permissionMapper.findRoleWithResourceByRoleId(role.getId());
            role.setResourceList(resources);
        });
        return roles;
    }

    @Override
    public void save(final RoleWithPermission role) {
        this.roleMapper.insert(role);
        this.rolePermissionMapper.saveRolePermission(role.getId(), role.getPermissionIdList());
    }

    @Override
    public void update(final RoleWithPermission role) {
        // 删掉所有权限，再添加回去
        final Condition condition = new Condition(RolePermission.class);
        condition.createCriteria().andCondition("role_id = ", role.getId());
        this.rolePermissionMapper.deleteByCondition(condition);
        this.rolePermissionMapper.saveRolePermission(role.getId(), role.getPermissionIdList());
    }
}
