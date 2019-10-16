package com.zoctan.api.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Component
public class RedisUtils {
  @Resource private RedisTemplate<String, Object> redisTemplate;

  // =============================common============================

  /**
   * 设置缓存失效时间
   *
   * @param key 键
   * @param timeout 时间
   * @return {Boolean}
   */
  public Boolean setExpire(@NotBlank final String key, @NotBlank final Duration timeout) {
    if (timeout.getSeconds() > 0) {
      return this.redisTemplate.expire(key, timeout.getSeconds(), TimeUnit.SECONDS);
    }
    return false;
  }

  /**
   * 获取缓存失效时间
   *
   * @param key 键
   * @return 时间（秒） 0为永久有效
   */
  public Long getExpire(@NotBlank final String key) {
    return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }

  /**
   * key 是否存在
   *
   * @param key 键
   * @return {Boolean}
   */
  public Boolean hasKey(@NotBlank final String key) {
    return this.redisTemplate.hasKey(key);
  }

  /**
   * 删除缓存
   *
   * @param keys 键
   */
  public Boolean delete(@NotBlank final String... keys) {
    return keys.length
        == Optional.ofNullable(this.redisTemplate.delete(Arrays.asList(keys))).orElse(-1L);
  }

  // ============================String=============================

  /**
   * 获取普通缓存
   *
   * @param key 键
   * @return 值
   */
  public Object getValue(@NotBlank final String key) {
    return this.redisTemplate.opsForValue().get(key);
  }

  /**
   * 设置普通缓存
   *
   * @param key 键
   * @param value 值
   */
  public void setValue(@NotBlank final String key, @NotBlank final Object value) {
    this.redisTemplate.opsForValue().set(key, value);
  }

  /**
   * 设置普通缓存
   *
   * @param key 键
   * @param value 值
   * @param timeout 时间 小于等于0时将设为无限期
   */
  public void setValue(
      @NotBlank final String key, @NotBlank final Object value, @NotBlank final Duration timeout) {
    this.redisTemplate.opsForValue().set(key, value, timeout);
  }

  /**
   * 递增
   *
   * @param key 键
   * @param delta 要增加几（大于0）
   * @return 加上指定值之后 key 的值
   */
  public Long incrementValue(@NotBlank final String key, @NotBlank final long delta) {
    if (delta > 0) {
      throw new RuntimeException("递增因子必须大于0");
    }
    return this.redisTemplate.opsForValue().increment(key, delta);
  }

  /**
   * 递减
   *
   * @param key 键
   * @param delta 要减少几(小于0)
   * @return 减少指定值之后 key 的值
   */
  public Long decrementValue(@NotBlank final String key, @NotBlank final long delta) {
    if (delta < 0) {
      throw new RuntimeException("递减因子必须大于0");
    }
    return this.redisTemplate.opsForValue().increment(key, -delta);
  }

  // ================================Map=================================

  /**
   * HashGet
   *
   * @param key 键
   * @param item 项
   * @return 值
   */
  public Object getHash(@NotBlank final String key, @NotBlank final String item) {
    return this.redisTemplate.opsForHash().get(key, item);
  }

  /**
   * 获取hashKey对应的所有键值
   *
   * @param key 键
   * @return 对应的多个键值
   */
  public Map<Object, Object> getHash(@NotBlank final String key) {
    return this.redisTemplate.opsForHash().entries(key);
  }

  /**
   * HashSet
   *
   * @param key 键
   * @param map 对应多个键值
   */
  public void putHash(@NotBlank final String key, @NotBlank final Map<String, Object> map) {
    this.redisTemplate.opsForHash().putAll(key, map);
  }

  /**
   * HashSet 并设置时间
   *
   * @param key 键
   * @param map 对应多个键值
   * @param timeout 时间
   */
  public void putHash(
      @NotBlank final String key,
      @NotBlank final Map<String, Object> map,
      @NotBlank final Duration timeout) {
    this.redisTemplate.opsForHash().putAll(key, map);
    this.setExpire(key, timeout);
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key 键
   * @param item 项
   * @param value 值
   */
  public void putHash(
      @NotBlank final String key, @NotBlank final String item, @NotBlank final Object value) {
    this.redisTemplate.opsForHash().put(key, item, value);
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key 键
   * @param item 项
   * @param value 值
   * @param timeout 时间 注意:如果已存在的hash表有时间,这里将会替换原有的时间
   */
  public void putHash(
      @NotBlank final String key,
      @NotBlank final String item,
      @NotBlank final Object value,
      @NotBlank final Duration timeout) {
    this.redisTemplate.opsForHash().put(key, item, value);
    this.setExpire(key, timeout);
  }

  /**
   * 删除hash表中的值
   *
   * @param key 键
   * @param item 项
   */
  public void deleteHash(@NotBlank final String key, @NotBlank final Object... item) {
    this.redisTemplate.opsForHash().delete(key, item);
  }

  /**
   * 判断hash表中是否有该项的值
   *
   * @param key 键
   * @param item 项
   * @return {Boolean}
   */
  public Boolean hasKeyHash(@NotBlank final String key, @NotBlank final String item) {
    return this.redisTemplate.opsForHash().hasKey(key, item);
  }

  /**
   * hash递增 如果不存在,就会创建一个 并把新增后的值返回
   *
   * @param key 键
   * @param item 项
   * @param by 要增加几(大于0)
   * @return 加上指定值之后 key 的值
   */
  public Double incrementHash(
      @NotBlank final String key, @NotBlank final String item, @NotBlank final double by) {
    return this.redisTemplate.opsForHash().increment(key, item, by);
  }

  /**
   * hash递减
   *
   * @param key 键
   * @param item 项
   * @param by 要减少记(小于0)
   * @return 减少指定值之后 key 的值
   */
  public Double decrementHash(
      @NotBlank final String key, @NotBlank final String item, @NotBlank final double by) {
    return this.redisTemplate.opsForHash().increment(key, item, -by);
  }

  // ============================set=============================

  /**
   * 根据 key 获取 Set 中的所有值
   *
   * @param key 键
   * @return Set<Object>
   */
  public Set<Object> getSet(@NotBlank final String key) {
    return this.redisTemplate.opsForSet().members(key);
  }

  /**
   * 根据 value 从一个 set 中查询,是否存在
   *
   * @param key 键
   * @param value 值
   * @return {Boolean}
   */
  public Boolean hasKeySet(@NotBlank final String key, @NotBlank final Object value) {
    return this.redisTemplate.opsForSet().isMember(key, value);
  }

  /**
   * 将数据放入set缓存
   *
   * @param key 键
   * @param values 值
   * @return 放入个数
   */
  public Long addSet(@NotBlank final String key, @NotBlank final Object... values) {
    return this.redisTemplate.opsForSet().add(key, values);
  }

  /**
   * 将set数据放入缓存
   *
   * @param key 键
   * @param timeout 时间
   * @param values 值
   * @return 放入个数
   */
  public Long addSet(
      @NotBlank final String key,
      @NotBlank final Duration timeout,
      @NotBlank final Object... values) {
    final Long num = this.redisTemplate.opsForSet().add(key, values);
    this.setExpire(key, timeout);
    return num;
  }

  /**
   * 获取set缓存的长度
   *
   * @param key 键
   * @return 缓存的长度
   */
  public Long getSetSize(@NotBlank final String key) {
    return this.redisTemplate.opsForSet().size(key);
  }

  /**
   * 移除值为value的
   *
   * @param key 键
   * @param values 值
   * @return 移除个数
   */
  public Long removeSet(@NotBlank final String key, @NotBlank final Object... values) {
    return this.redisTemplate.opsForSet().remove(key, values);
  }
  // ===============================list=================================

  /**
   * 获取list缓存的内容
   *
   * @param key 键
   * @param start 开始
   * @param end 结束 0 到 -1代表所有值
   * @return list缓存的内容
   */
  public List<Object> getList(
      @NotBlank final String key, @NotBlank final Long start, @NotBlank final Long end) {
    return this.redisTemplate.opsForList().range(key, start, end);
  }

  /**
   * 获取list缓存的长度
   *
   * @param key 键
   * @return list缓存的长度
   */
  public Long getListSize(@NotBlank final String key) {
    return this.redisTemplate.opsForList().size(key);
  }

  /**
   * 通过索引 获取list中的值
   *
   * @param key 键
   * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
   * @return list中的值
   */
  public Object getListIndex(@NotBlank final String key, @NotBlank final Long index) {
    return this.redisTemplate.opsForList().index(key, index);
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @return 放入个数
   */
  public Long pushList(@NotBlank final String key, @NotBlank final Object value) {
    return this.redisTemplate.opsForList().rightPush(key, value);
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @param timeout 时间
   */
  public Long pushList(
      @NotBlank final String key, @NotBlank final Object value, @NotBlank final Duration timeout) {
    final Long num = this.redisTemplate.opsForList().rightPush(key, value);
    this.setExpire(key, timeout);
    return num;
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @return 放入个数
   */
  public Long pushList(@NotBlank final String key, @NotBlank final List<Object> value) {
    return this.redisTemplate.opsForList().rightPushAll(key, value);
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @param timeout 时间
   * @return 放入个数
   */
  public Long pushList(
      @NotBlank final String key,
      @NotBlank final List<Object> value,
      @NotBlank final Duration timeout) {
    final Long num = this.redisTemplate.opsForList().rightPushAll(key, value);
    this.setExpire(key, timeout);
    return num;
  }

  /**
   * 根据索引修改 list 中的某条数据
   *
   * @param key 键
   * @param index 索引
   * @param value 值
   */
  public void updateListIndex(
      @NotBlank final String key, @NotBlank final Long index, @NotBlank final Object value) {
    this.redisTemplate.opsForList().set(key, index, value);
  }

  /**
   * 移除N个值为value
   *
   * @param key 键
   * @param count 移除多少个
   * @param value 值
   * @return 移除个数
   */
  public Long removeList(
      @NotBlank final String key, @NotBlank final Long count, @NotBlank final Object value) {
    return this.redisTemplate.opsForList().remove(key, count, value);
  }
}
