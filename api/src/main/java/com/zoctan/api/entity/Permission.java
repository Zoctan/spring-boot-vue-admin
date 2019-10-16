package com.zoctan.api.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Data
public class Permission {
  /** 权限Id */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 权限对应的资源 */
  private String resource;

  /** 权限的代码/通配符,对应代码中@hasAuthority(xx) */
  private String code;

  /** 对应的资源操作 */
  private String handle;
}
