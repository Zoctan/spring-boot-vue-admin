package com.zoctan.api.core.service;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service层基础接口，其他Service接口 请继承该接口
 *
 * @author Zoctan
 * @date 2018/05/07
 */
public interface Service<T> {
    /**
     * 持久化
     *
     * @param model 实体
     */
    void save(T model);

    /**
     * 持久化
     *
     * @param models 实体
     */
    void save(List<T> models);

    /**
     * 通过主鍵刪除
     *
     * @param id id
     */
    void deleteById(Object id);

    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）刪除
     *
     * @param fieldName 数据库中的字段名
     * @param value     字段对应的值
     * @throws TooManyResultsException TooManyResultsException
     */
    void deleteBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * 批量刪除 eg：ids -> “1,2,3,4”
     *
     * @param ids 多个Id
     */
    void deleteByIds(String ids);

    /**
     * 根据条件刪除
     *
     * @param condition 条件
     */
    void deleteByCondition(Condition condition);

    /**
     * 根据对象刪除
     *
     * @param model 实体
     */
    void delete(T model);

    /**
     * 更新
     *
     * @param model 实体
     */
    void update(T model);

    /**
     * 根据条件更新
     *
     * @param model     实体
     * @param condition 条件
     */
    void updateByCondition(T model, Condition condition);

    /**
     * 通过ID查找
     *
     * @param id id
     * @return T
     */
    T findById(Object id);

    /**
     * 通过Model中某个成员变量名称查找,value需符合unique约束
     *
     * @param fieldName 数据库中的字段名
     * @param value     字段对应的值
     * @return T
     * @throws TooManyResultsException TooManyResultsException
     */
    T findBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * 通过多个ID查找//eg：ids -> “1,2,3,4”
     *
     * @param ids 多个Id
     * @return List<T>
     */
    List<T> findByIds(String ids);

    /**
     * 根据条件查找
     *
     * @param condition 条件
     * @return List<T>
     */
    List<T> findByCondition(Condition condition);

    /**
     * 获取所有
     *
     * @return List<T>
     */
    List<T> findAll();

    /**
     * 计数
     *
     * @param condition 　条件
     * @return 数量
     */
    int countByCondition(Condition condition);
}
