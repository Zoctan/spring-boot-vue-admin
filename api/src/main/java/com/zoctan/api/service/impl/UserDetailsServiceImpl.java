package com.zoctan.api.service.impl;

import com.zoctan.api.model.User;
import com.zoctan.api.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zoctan on 2018/02/04.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final User user = this.userService.findDetailByUsername(username);
        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 角色
        authorities.add(new SimpleGrantedAuthority(user.getRoleName()));
        // 权限
        for (final String permissionCode : user.getPermissionCodeList()) {
            authorities.add(new SimpleGrantedAuthority(permissionCode));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
