package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.RoleMapper;
import com.zoctan.api.model.Role;
import com.zoctan.api.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Zoctan on 2018/02/17.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> findAllRoleWithPermission() {
        return this.roleMapper.findAllRoleWithPermission();
    }
}
