package com.zoctan.api.service.impl;

import com.zoctan.api.dto.AccountWithRolePermission;
import com.zoctan.api.service.AccountService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountDetailsServiceImpl implements UserDetailsService {
  @Resource private AccountService accountService;

  @Override
  public UserDetails loadUserByUsername(final String name) {
    final AccountWithRolePermission accountWithRolePermission =
        this.accountService.findDetailByName(name);
    // 权限
    final List<SimpleGrantedAuthority> authorities =
        accountWithRolePermission.getPermissionCodeList().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    // 角色
    authorities.add(new SimpleGrantedAuthority(accountWithRolePermission.getRoleName()));
    return new org.springframework.security.core.userdetails.User(
        accountWithRolePermission.getName(), "", authorities);
  }
}
