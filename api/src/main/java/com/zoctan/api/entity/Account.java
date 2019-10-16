package com.zoctan.api.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Data
public class Account {
  /** 用户Id */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 邮箱 */
  private String email;

  /** 用户名 */
  private String name;

  /** 密码 */
  private String password;

  /** 注册时间 */
  private Timestamp registerTime;

  /** 上一次登录时间 */
  private Timestamp loginTime;
}
