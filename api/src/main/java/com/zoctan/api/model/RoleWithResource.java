package com.zoctan.api.model;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
public class RoleWithResource extends Role {
    /**
     * 角色对应的权限
     */
    @Transient
    private List<Resource> resourceList;

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }
}