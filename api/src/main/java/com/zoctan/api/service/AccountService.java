package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.dto.AccountDto;
import com.zoctan.api.dto.AccountWithRole;
import com.zoctan.api.dto.AccountWithRolePermission;
import com.zoctan.api.entity.Account;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface AccountService extends Service<Account> {
  /**
   * 保存用户
   *
   * @param accountDto 用户
   */
  void save(AccountDto accountDto);

  /**
   * 获取所有用户以及对应角色
   *
   * @return 用户列表
   */
  List<AccountWithRole> listAllWithRole();

  /**
   * 按条件查询用户
   *
   * @param params 参数
   * @return 用户列表
   */
  List<AccountWithRole> findWithRoleBy(final Map<String, Object> params);

  /**
   * 按条件查询用户信息
   *
   * @param column 列名
   * @param params 参数
   * @return 用户
   */
  AccountWithRolePermission findDetailBy(String column, Object params);

  /**
   * 按用户名查询用户信息
   *
   * @param name 用户名
   * @return 用户
   * @throws UsernameNotFoundException 用户名找不到
   */
  AccountWithRolePermission findDetailByName(String name) throws UsernameNotFoundException;

  /**
   * 按用户名更新最后一次登录时间
   *
   * @param name 用户名
   */
  void updateLoginTimeByName(String name);

  /**
   * 验证用户密码
   *
   * @param rawPassword 原密码
   * @param encodedPassword 加密后的密码
   * @return boolean
   */
  boolean verifyPassword(String rawPassword, String encodedPassword);
}
