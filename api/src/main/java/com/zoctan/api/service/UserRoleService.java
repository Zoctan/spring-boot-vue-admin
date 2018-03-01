package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.model.User;
import com.zoctan.api.model.UserRole;

/**
 * Created by Zoctan on 2018/02/17.
 */
public interface UserRoleService extends Service<UserRole> {
    void updateUserRole(User user);
}
