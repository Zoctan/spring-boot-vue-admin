package com.zoctan.api.service.impl;

import com.zoctan.api.core.exception.ServiceException;
import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.AuthMapper;
import com.zoctan.api.mapper.RoleMapper;
import com.zoctan.api.mapper.UserMapper;
import com.zoctan.api.mapper.UserRoleMapper;
import com.zoctan.api.model.Role;
import com.zoctan.api.model.User;
import com.zoctan.api.model.UserRole;
import com.zoctan.api.service.UserService;
import com.zoctan.api.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private void saveRole(final long id, final String roleName) {
        final Condition condition = new Condition(Role.class);
        condition.createCriteria().andCondition("name = ", roleName);
        final Role role = this.roleMapper.selectByCondition(condition).get(0);
        this.userRoleMapper.insert(
                new UserRole()
                        .setUserId(id)
                        .setRoleId(role.getId()));
    }

    /**
     * 重写save方法，密码加密后再存
     */
    @Override
    public void save(final User user) {
        User u = this.findBy("username", user.getUsername());
        if (u != null) {
            throw new ServiceException("用户名已存在");
        } else {
            u = this.findBy("email", user.getEmail());
            if (u != null) {
                throw new ServiceException("邮箱已存在");
            } else {
                //log.info("before password : {}", user.getPassword().trim());
                user.setPassword(this.passwordEncoder.encode(user.getPassword().trim()));
                //log.info("after password : {}", user.getPassword());
                user.setRegisterTime(DateUtil.getNowTimestamp());
                this.userMapper.insertSelective(user);
                //log.info("User<{}> id : {}", user.getUsername(), user.getId());
                this.saveRole(user.getId(), "ROLE_USER");
            }
        }
    }

    /**
     * 重写update方法
     */
    @Override
    public void update(final User user) {
        // 如果修改了密码
        if (user.getPassword() != null) {
            // 密码修改后需要加密
            user.setPassword(this.passwordEncoder.encode(user.getPassword().trim()));
        }
        this.userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> findAllUserWithRole() {
        return this.userMapper.findAllUserWithRole();
    }

    @Override
    public User findDetailBy(final String column, final Object param) {
        final Map<String, Object> map = new HashMap<>();
        map.put(column, param);
        return this.userMapper.findDetailBy(map);
    }

    @Override
    public User findDetailByUsername(final String username) throws UsernameNotFoundException {
        final User user = this.findDetailBy("username", username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (user.getRoleName().equals("ROLE_ADMIN")) {
            // 超级管理员所有权限都有
            user.setAuthList(this.authMapper.findAllCode());
        }
        return user;
    }

    @Override
    public boolean verifyPassword(final String rawPassword, final String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public void updateLoginTime(final String username) {
        final Condition condition = new Condition(User.class);
        condition.createCriteria()
                .andCondition("username = ", username);
        final User user = new User();
        user.setLastLoginTime(DateUtil.getNowTimestamp());
        this.userMapper.updateByConditionSelective(user, condition);
    }
}
