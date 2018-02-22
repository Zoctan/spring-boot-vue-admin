package com.zoctan.api.core.service;


import com.zoctan.api.core.exception.ServiceException;
import com.zoctan.api.core.mapper.MyMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T> implements Service<T> {
    // 当前泛型真实类型的Class
    private final Class<T> modelClass;
    @Autowired
    protected MyMapper<T> mapper;

    public AbstractService() {
        final ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public void save(final T model) {
        this.mapper.insertSelective(model);
    }

    @Override
    public void save(final List<T> models) {
        this.mapper.insertList(models);
    }

    @Override
    public void deleteById(final Object id) {
        this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteBy(final String fieldName, final Object value) throws TooManyResultsException {
        try {
            final T model = this.modelClass.newInstance();
            final Field field = this.modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            this.mapper.delete(model);
        } catch (final ReflectiveOperationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteByIds(final String ids) {
        this.mapper.deleteByIds(ids);
    }

    @Override
    public void deleteByCondition(final Condition condition) {
        this.mapper.deleteByCondition(condition);
    }

    @Override
    public void update(final T model) {
        this.mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public void updateByCondition(final T model, final Condition condition) {
        this.mapper.updateByConditionSelective(model, condition);
    }

    @Override
    public T findById(final Object id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    @Override
    public T findBy(final String fieldName, final Object value) throws TooManyResultsException {
        try {
            final T model = this.modelClass.newInstance();
            final Field field = this.modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return this.mapper.selectOne(model);
        } catch (final ReflectiveOperationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<T> findByIds(final String ids) {
        return this.mapper.selectByIds(ids);
    }

    @Override
    public List<T> findByCondition(final Condition condition) {
        return this.mapper.selectByCondition(condition);
    }

    @Override
    public List<T> findAll() {
        return this.mapper.selectAll();
    }
}
