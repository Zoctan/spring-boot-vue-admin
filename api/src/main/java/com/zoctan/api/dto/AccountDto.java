package com.zoctan.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Data
public class AccountDto {
  /** 用户Id */
  private Long id;

  /** 邮箱 */
  @NotEmpty(message = "邮箱不能为空")
  @Email
  private String email;

  /** 用户名 */
  @NotEmpty(message = "用户名不能为空")
  @Size(min = 3, message = "用户名长度不能小于3")
  private String name;

  /** 密码 */
  @JSONField(serialize = false)
  @NotEmpty(message = "密码不能为空")
  @Size(min = 6, message = "密码长度不能小于6")
  private String password;

  /** 注册时间 */
  private Timestamp registerTime;

  /** 上一次登录时间 */
  private Timestamp loginTime;

  private Long roleId;
}
