package com.zoctan.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.PermissionMapper;
import com.zoctan.api.model.Permission;
import com.zoctan.api.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Zoctan on 2018/02/17.
 */
@Service
@Transactional
public class PermissionServiceImpl extends AbstractService<Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<JSONObject> findAllSameResource() {
        return this.permissionMapper.findAllSameResource();
    }
}
