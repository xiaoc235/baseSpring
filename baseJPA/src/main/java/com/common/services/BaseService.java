package com.common.services;

import com.common.base.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 基本操作
 * @author jianghaoming
 * @date 2019-03-26 15:49:33
 */
@NoRepositoryBean
public interface BaseService<T,ID> {


    /**
     * 保存信息
     * @param dto
     * @return
     */
    T save(T dto);

    /**
     * 主键查询
     * @param id
     * @return
     */
    T getById(ID id);

    /**
     * 主键查询，并检查是否存在
     * @param id
     * @return
     * @throws BusinessException
     */
    T getByIdAndCheck(ID id) throws BusinessException;

    /**
     * 根据条件查询
     * @param query
     * @return
     */
    T getByQuery(T query);

    /**
     * 根据主键修改数据
     * @param id
     * @param query
     * @return
     * @throws BusinessException
     */
    void updateById(ID id, T query) throws BusinessException;

    /**
     * 删除数据
     * @param id
     * @throws BusinessException
     */
    void deleteById(ID id) throws BusinessException;

    /**
     * 查询所有数据
     * @param query
     * @return
     */
    List<T> getAllByQuery(T query);

    /**
     * 查询所有数据，分页
     * @param query
     * @param pageable
     * @return
     */
    Page<T> getAllByPage(T query, Pageable pageable);

}
