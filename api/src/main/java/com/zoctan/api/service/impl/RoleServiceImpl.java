package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.RoleMapper;
import com.zoctan.api.mapper.RolePermissionMapper;
import com.zoctan.api.model.Role;
import com.zoctan.api.model.RolePermission;
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
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Role> findAllRoleWithPermission() {
        return this.roleMapper.findAllRoleWithPermission();
    }

    @Override
    public void save(final Role role) {
        this.roleMapper.insert(role);
        this.rolePermissionMapper.saveRolePermission(role.getId(), role.getPermissionIdList());
    }

    @Override
    public void update(final Role role) {
        // 删掉所有权限，再添加回去
        final Condition condition = new Condition(RolePermission.class);
        condition.createCriteria().andCondition("role_id = ", role.getId());
        this.rolePermissionMapper.deleteByCondition(condition);
        this.rolePermissionMapper.saveRolePermission(role.getId(), role.getPermissionIdList());
    }
}
