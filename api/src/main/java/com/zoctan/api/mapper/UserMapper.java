package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.model.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends MyMapper<User> {
    List<User> findAllUserWithRole();

    User findDetailBy(Map<String, Object> param);
}