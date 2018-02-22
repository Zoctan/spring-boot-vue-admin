package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.model.Auth;

import java.util.List;

public interface AuthMapper extends MyMapper<Auth> {
    List<String> findAllCode();
}