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
    /**
     * 获取所有用户以及对应角色
     *
     * @return 用户列表
     */
    List<User> findAllUserWithRole();

    /**
     * 按条件查询用户信息
     *
     * @param column 列名
     * @param param  参数map
     * @return 用户
     */
    User findDetailBy(String column, Object param);


    /**
     * 按用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户
     * @throws UsernameNotFoundException 用户名找不到
     */
    User findDetailByUsername(String username) throws UsernameNotFoundException;

    /**
     * 按用户名更新最后一次登录时间
     *
     * @param username 用户名
     */
    void updateLoginTimeByUsername(String username);

    /**
     * 验证用户密码
     *
     * @param rawPassword     原密码
     * @param encodedPassword 加密后的密码
     * @return boolean
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);
}
