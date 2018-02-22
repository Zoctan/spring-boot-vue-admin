package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.AuthMapper;
import com.zoctan.api.model.Auth;
import com.zoctan.api.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Zoctan on 2018/02/17.
 */
@Service
@Transactional
public class AuthServiceImpl extends AbstractService<Auth> implements AuthService {
    @Resource
    private AuthMapper authMapper;

}
