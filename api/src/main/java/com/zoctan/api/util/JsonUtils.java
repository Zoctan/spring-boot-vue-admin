package com.zoctan.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.Arrays;

/**
 * Json工具
 *
 * @author Zoctan
 * @date 2018/07/11
 */
public class JsonUtils {
  private JsonUtils() {}

  /**
   * 保留某些字段
   *
   * @param target 目标对象
   * @param fields 字段
   * @return 保留字段后的对象
   */
  public static <T> T keepFields(final Object target, final Class<T> clz, final String... fields) {
    final SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
    filter.getIncludes().addAll(Arrays.asList(fields));
    return done(target, clz, filter);
  }

  /**
   * 去除某些字段
   *
   * @param target 目标对象
   * @param fields 字段
   * @return 去除字段后的对象
   */
  public static <T> T deleteFields(
      final Object target, final Class<T> clz, final String... fields) {
    final SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
    filter.getExcludes().addAll(Arrays.asList(fields));
    return done(target, clz, filter);
  }

  private static <T> T done(
      final Object target, final Class<T> clz, final SimplePropertyPreFilter filter) {
    final String jsonString = JSON.toJSONString(target, filter);
    return JSON.parseObject(jsonString, clz);
  }
}
