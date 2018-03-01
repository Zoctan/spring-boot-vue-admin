package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.UserRoleMapper;
import com.zoctan.api.model.User;
import com.zoctan.api.model.UserRole;
import com.zoctan.api.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;

/**
 * Created by Zoctan on 2018/02/17.
 */
@Service
@Transactional
public class UserRoleServiceImpl extends AbstractService<UserRole> implements UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public void updateUserRole(final User user) {
        final Condition condition = new Condition(UserRole.class);
        condition.createCriteria().andCondition("user_id = ", user.getId());
        final UserRole userRole = new UserRole()
                .setUserId(user.getId())
                .setRoleId(user.getRoleId());
        this.userRoleMapper.updateByConditionSelective(userRole, condition);
    }
}
