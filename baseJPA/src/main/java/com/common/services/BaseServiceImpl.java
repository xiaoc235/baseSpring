package com.common.services;

import com.common.base.exception.BusinessException;
import com.common.base.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 基本操作实现
 * @author jianghaoming
 * @date 2019-03-26 15:09:34
 */
@NoRepositoryBean
public class BaseServiceImpl<T,ID>  implements BaseService<T,ID> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JpaRepository<T, ID> jpaRepository;



    @Override
    public T save(T dto) {
        return jpaRepository.save(dto);
    }

    @Override
    public T getById(ID id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public T getByIdAndCheck(ID id) throws BusinessException {
        T result = this.getById(id);
        if(result == null){
            throw new NotFoundException("id :" + id);
        }
        return result;
    }

    @Override
    public T getByQuery(T query) {
       return jpaRepository.findOne(Example.of(query)).orElse(null);
    }



    @Override
    public void updateById(ID id, T query) throws BusinessException {
        this.getByIdAndCheck(id);
        jpaRepository.save(query);
    }

    @Override
    public void deleteById(ID id) throws BusinessException {
        this.getByIdAndCheck(id);
        jpaRepository.deleteById(id);
    }

    @Override
    public List<T> getAllByQuery(T query) {
        if(query == null){
            return jpaRepository.findAll();
        }
        return jpaRepository.findAll(Example.of(query));
    }

    @Override
    public Page<T> getAllByPage(T query, Pageable pageable) {
        if(query == null){
            return jpaRepository.findAll(pageable);
        }
        return jpaRepository.findAll(Example.of(query), pageable);
    }

}
