package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.RoleAuthMapper;
import com.zoctan.api.model.RoleAuth;
import com.zoctan.api.service.RoleAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Zoctan on 2018/02/17.
 */
@Service
@Transactional
public class RoleAuthServiceImpl extends AbstractService<RoleAuth> implements RoleAuthService {
    @Resource
    private RoleAuthMapper roleAuthMapper;

}
