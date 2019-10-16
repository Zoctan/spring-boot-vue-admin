package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.AccountRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface AccountRoleMapper extends MyMapper<AccountRole> {

  /**
   * 更新用户角色
   *
   * @param accountRole 用户角色
   */
  @Update(
      "UPDATE account_role SET role_id = #{accountRole.roleId} WHERE account_id = #{accountRole.accountId}")
  void updateRoleIdByAccountId(@Param("accountRole") AccountRole accountRole);
}
