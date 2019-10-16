package com.zoctan.api.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Data
public class Role {
  /** 角色Id */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 角色名称 */
  @NotEmpty(message = "角色名不能为空")
  private String name;

  /** 创建时间 */
  @Column(name = "create_time")
  private Timestamp createTime;

  /** 修改时间 */
  @Column(name = "update_time")
  private Timestamp updateTime;
}
