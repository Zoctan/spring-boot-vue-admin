package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.AccountRole;
import com.zoctan.api.service.AccountRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@RestController
@RequestMapping("/account/role")
public class AccountRoleController {
  @Resource private AccountRoleService accountRoleService;

  @PreAuthorize("hasAuthority('role:update')")
  @PutMapping
  public Result updateAccountRole(
      @RequestBody final AccountRole accountRole, final Principal principal) {
    final AccountRole dbAccountRole =
        this.accountRoleService.getBy("accountId", accountRole.getAccountId());
    this.accountRoleService.updateRoleIdByAccountId(accountRole);
    return ResultGenerator.genOkResult();
  }
}
