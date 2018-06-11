package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.model.User;
import com.zoctan.api.model.UserRole;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface UserRoleService extends Service<UserRole> {
    /**
     * 更新用户角色
     *
     * @param user 用户
     */
    void updateUserRole(User user);
}
