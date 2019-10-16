package com.zoctan.api.dto;

import com.zoctan.api.entity.Resource;
import com.zoctan.api.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleWithResource extends Role {
  /** 角色对应的权限 */
  @Transient private List<Resource> resourceList;
}
