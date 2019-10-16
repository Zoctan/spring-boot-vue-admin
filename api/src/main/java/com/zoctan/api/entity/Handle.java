package com.zoctan.api.entity;

import lombok.Data;

import javax.persistence.Transient;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
@Data
public class Handle {
  /** 对应权限id */
  @Transient private Long id;

  /** 操作名称 */
  @Transient private String handle;
}
