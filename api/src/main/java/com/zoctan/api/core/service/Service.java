package com.zoctan.api.core.service;

import com.zoctan.api.core.exception.ResourcesNotFoundException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service 层基础接口
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public interface Service<T> {

  /**
   * 确保实体存在
   *
   * @param id 实体id
   * @throws ResourcesNotFoundException 不存在实体异常
   */
  void assertById(Object id);

  /**
   * 确保实体存在
   *
   * @param entity 实体
   * @throws ResourcesNotFoundException 不存在实体异常
   */
  void assertBy(T entity);

  /**
   * 确保实体存在
   *
   * @param ids ids
   */
  void assertByIds(String ids);

  /**
   * 根据 ids 获取实体数
   *
   * @param ids ids
   */
  int countByIds(String ids);

  /**
   * 根据条件获取实体数
   *
   * @param condition 条件
   */
  int countByCondition(Condition condition);

  /**
   * 持久化
   *
   * @param entity 实体
   */
  void save(T entity);

  /**
   * 批量持久化
   *
   * @param entities 实体列表
   */
  void save(List<T> entities);

  /**
   * 通过主鍵刪除
   *
   * @param id id
   */
  void deleteById(Object id);

  /**
   * 通过实体中某个成员变量名称（非数据表中 column 的名称）刪除
   *
   * @param fieldName 字段名
   * @param value 字段值
   * @throws TooManyResultsException 多条结果异常
   */
  void deleteBy(String fieldName, Object value) throws TooManyResultsException;

  /**
   * 批量刪除 ids -> “1,2,3,4”
   *
   * @param ids ids
   */
  void deleteByIds(String ids);

  /**
   * 根据条件刪除
   *
   * @param condition 条件
   */
  void deleteByCondition(Condition condition);

  /**
   * 按组件更新
   *
   * @param entity 实体
   */
  void update(T entity);

  /**
   * 按条件更新
   *
   * @param entity 实体
   * @param condition 条件
   */
  void updateByCondition(T entity, Condition condition);

  /**
   * 通过 id 查找
   *
   * @param id id
   * @return 实体
   */
  T getById(Object id);

  /**
   * 通过实体中某个成员变量名称查找 value 需符合 unique 约束
   *
   * @param fieldName 字段名
   * @param value 字段值
   * @return 实体
   * @throws TooManyResultsException 多条结果异常
   */
  T getBy(String fieldName, Object value) throws TooManyResultsException;

  /**
   * 通过多个 id 查找 ids -> “1,2,3,4”
   *
   * @param ids ids
   * @return 实体列表
   */
  List<T> listByIds(String ids);

  /**
   * 按条件查找
   *
   * @param condition 条件
   * @return 实体列表
   */
  List<T> listByCondition(Condition condition);

  /**
   * 获取所有实体
   *
   * @return 实体列表
   */
  List<T> listAll();
}
