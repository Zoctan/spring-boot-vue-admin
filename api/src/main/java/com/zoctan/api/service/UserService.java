package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface UserService extends Service<User> {
    @Override
    void save(User user);

    List<User> findAllUserWithRole();

    @Override
    void update(User user);

    User findDetailBy(String column, Object param);

    User findDetailByUsername(String username) throws UsernameNotFoundException;

    void updateLoginTime(String username);

    boolean verifyPassword(String rawPassword, String encodedPassword);
}
