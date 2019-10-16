package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.dto.AccountWithRole;
import com.zoctan.api.dto.AccountWithRolePermission;
import com.zoctan.api.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface AccountMapper extends MyMapper<Account> {
  /**
   * 获取所有用户以及对应角色
   *
   * @return 用户列表
   */
  List<AccountWithRole> listAllWithRole();

  /**
   * 按微信小程序Id获取用户
   *
   * @return 用户
   */
  Account findByWechatOpenId(@Param("openId") String openId);

  /**
   * 按条件获取用户
   *
   * @param params 参数
   * @return 用户列表
   */
  List<AccountWithRole> findWithRoleBy(final Map<String, Object> params);

  /**
   * 按条件查询用户信息
   *
   * @param params 参数
   * @return 用户
   */
  AccountWithRolePermission findDetailBy(Map<String, Object> params);

  /**
   * 按用户名更新最后登陆时间
   *
   * @param name 用户名
   */
  void updateLoginTimeByName(@Param("name") String name);
}
