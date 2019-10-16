package com.zoctan.api.core.mapper;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * 定制版 MyBatis Mapper 插件接口，如需其他接口参考官方文档自行添加
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public interface MyMapper<T>
    extends BaseMapper<T>, ConditionMapper<T>, IdsMapper<T>, InsertListMapper<T> {}
