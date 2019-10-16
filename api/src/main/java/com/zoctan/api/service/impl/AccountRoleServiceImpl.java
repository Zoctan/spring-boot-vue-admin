package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.AccountRole;
import com.zoctan.api.mapper.AccountRoleMapper;
import com.zoctan.api.service.AccountRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountRoleServiceImpl extends AbstractService<AccountRole>
    implements AccountRoleService {
  @Resource private AccountRoleMapper accountRoleMapper;

  @Override
  public void updateRoleIdByAccountId(final AccountRole accountRole) {
    this.accountRoleMapper.updateRoleIdByAccountId(accountRole);
  }
}
