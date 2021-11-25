package com.somnus.microservice.commons.core.support;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.somnus.microservice.commons.base.dto.LoginAuthDto;
import com.somnus.microservice.commons.core.mybatis.BaseEntity;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.support
 * @title: IService
 * @description: TODO
 * @date 2019/4/16 17:32
 * @param <T> the type parameter
 */
public interface IService<T> {

    LoginAuthDto getLoginAuth();

    default <DTO, R extends BaseEntity> R convert(DTO dto, Function<DTO, ? extends R> function){

        BaseEntity entity = function.apply(dto);

        entity.setUpdateInfo(getLoginAuth());

        return (R)entity;
    }

    /**
     * 根据实体中的属性值进行查询, 查询条件使用等号  @param record the record
     *
     * @param wrapper the wrapper
     *
     * @return the list
     */
    List<T> select(Wrapper<T> wrapper);

    /**
     * 根据主键字段进行查询, 方法参数必须包含完整的主键属性, 查询条件使用等号  @param key the key
     *
     * @param key the key
     *
     * @return the t
     */
    T selectByKey(Serializable key);

    /**
     * 根据实体中的属性进行查询, 只能有一个返回值, 有多个结果是抛出异常, 查询条件使用等号  @param record the record
     *
     * @param wrapper the wrapper
     *
     * @return the t
     */
    T selectOne(Wrapper<T> wrapper);

    /**
     * 根据实体中的属性查询总数, 查询条件使用等号  @param record the record
     *
     * @param wrapper the wrapper
     *
     * @return the int
     */
    int selectCount(Wrapper<T> wrapper);

    /**
     * 保存一个实体, null的属性不会保存, 会使用数据库默认值  @param record the record
     *
     * @param record the record
     *
     * @return the int
     */
    int save(T record);

    /**
     * 根据主键更新属性不为null的值  @param entity the entity
     *
     * @param entity the entity
     *
     * @return the int
     */
    int update(T entity);

    /**
     * 根据实体属性作为条件进行删除, 查询条件使用等号  @param record the record
     *
     * @param wrapper the record
     *
     * @return the int
     */
    int delete(Wrapper<T> wrapper);

    /**
     * 根据主键字段进行删除, 方法参数必须包含完整的主键属性  @param key the key
     *
     * @param key the key
     *
     * @return the int
     */
    int deleteByKey(Serializable key);

    /**
     * 根据Wrapper条件更新实体record包含的不是null的属性值  @param record the record
     *
     * @param record  the record
     * @param wrapper the wrapper
     *
     * @return the int
     */
    int updateByWrapper(@Param("record") T record,  Wrapper<T> wrapper);

}