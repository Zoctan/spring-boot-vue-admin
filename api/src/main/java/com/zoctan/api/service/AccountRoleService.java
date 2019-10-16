package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.AccountRole;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface AccountRoleService extends Service<AccountRole> {
  /**
   * 更新用户角色
   *
   * @param accountRole 用户角色
   */
  void updateRoleIdByAccountId(AccountRole accountRole);
}
