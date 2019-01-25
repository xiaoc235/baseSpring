package com.common.dao;

import com.common.utils.CommonUtils;
import com.google.gson.JsonObject;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jianghaoming
 * @date 2017/3/1523:08
 */

@Transactional
@Repository
@Qualifier("BaseDao")
public abstract class BaseDaoImpl {

    private static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    private static final String LIMIT_SQL = " limit ";
    private static final String ORDER_BY_SQL = " order by ";

    @PersistenceContext(unitName = "entityManagerFactory")
    EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * * 查询数据集合,不分页T
     */
    protected <T> List<T> queryListEntity(String sql, Map<String, Object> params, Class<T> clazz){
        List<T> resultList = null;
        try {
            resultList = this.queryList(sql,params, clazz);
            if(resultList.isEmpty()){
                return new ArrayList<>();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resultList = new ArrayList<>();
        }
        return resultList;
    }


    /**
     * 查询单条数据
     * 通过list来转换
     */
    protected  <T> T queryEntity(String sql, Map<String, Object> params, Class<T> clazz){
        List<T> resultList = this.queryList(sql,params, clazz);
        if(!resultList.isEmpty()){
            return resultList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 查询数据集合，分页
     */
    protected <T> Page<T> queryListEntityByPage(String sql, Map<String, Object> params, Class<T> clazz, Pageable pageable){
        PageImpl<T> pageResult = null;
        if(null == pageable){
            List<T> resultList = this.queryListEntity(sql,params,clazz);
            pageResult = new PageImpl<>(resultList);
            return pageResult;
        }

        int pageNumber = pageable.getPageNumber();
        if(pageNumber<0){
            pageNumber = 0;
        }
        final int pageSize = pageable.getPageSize();

        //排序sql拼接 order by
        StringBuilder pageSql = new StringBuilder(sql);
        Sort sort = pageable.getSort();
        if(sort.isSorted()) {
            pageSql.append(ORDER_BY_SQL);
            for (Sort.Order order : sort) {
                pageSql.append(CommonUtils.underscoreName(order.getProperty())).append(" ").append(order.getDirection());
            }
        }

        //分页sql拼接 limit
        pageSql.append(LIMIT_SQL);
        pageSql.append(pageNumber * pageSize).append(",").append(pageSize);

        String querySql = pageSql.toString();
        List<T> resultList = this.queryListEntity(querySql,params,clazz);
        final int totalCount = this.getCountBy("select count(1) from ("+sql+") t",params);
        pageResult = new PageImpl<>(resultList,pageable,totalCount);

        return pageResult;
    }

    /**
     * 获取记录条数
     */
    protected Integer getCountBy(String sql,Map<String, Object> params){
        BigInteger bigInteger  = (BigInteger) this.query(sql,params).getSingleResult();
        return bigInteger.intValue();
    }


    /**
     * 新增或者删除
     * @param sql
     * @param params
     * @return
     */
    protected Integer execute(String sql,Map<String, Object> params){
        return this.query(sql,params).executeUpdate();
    }



    private Query query(final String sql,  Map<String, Object> params){
        Query query =  entityManager.createNativeQuery(sql);
        logger.info("sql :{} ", sql);
        if (params != null) {
            StringBuilder paramStr = new StringBuilder();

            for(Map.Entry<String,Object> entry : params.entrySet()){
                paramStr.append(entry.getKey()+"="+entry.getValue()+" ");
                query.setParameter(entry.getKey(), entry.getValue());
            }
            logger.info("params [{}]",paramStr);
        }
        return query;
    }

    private <T> NativeQuery<T> getSqlQuery(final String sql, Map<String, Object> params, Class<T> tClass){
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<T> query = session.createNativeQuery(sql, tClass);
        logger.info("sql :{} ", sql);
        if (params != null) {
            StringBuilder paramStr = new StringBuilder();
            for(Map.Entry<String,Object> entry : params.entrySet()){
                paramStr.append(entry.getKey()).append("=").append(entry.getValue()).append(" ");
                query.setParameter(entry.getKey(), entry.getValue());
            }
            logger.info("params :{}",paramStr);
        }
        return query;
    }


    private <T> List<T> queryList(String sql, Map<String, Object> params, Class<T> tClass){
        NativeQuery<T> query = this.getSqlQuery(sql,params,tClass);
        List<T> resultList = query.getResultList();
        if(resultList == null){
            resultList = new ArrayList<>();
        }
        return resultList;
    }


    /**
     * map 去除下划线，并转换为jsonObject
     * @user jianghaoming
     * @date 2017/11/13  下午4:45
     *
     */
    private JsonObject convertJsonObject(Map<String,Object> map){
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String,Object> entry : map.entrySet()){
            if(entry.getValue()!=null) {
                String key = CommonUtils.camelName(entry.getKey());
                String classTypeName = entry.getValue().getClass().getTypeName();
                if("java.lang.Integer".equals(classTypeName)){
                    jsonObject.addProperty(key, Integer.parseInt(entry.getValue()+""));
                }else if("java.sql.Timestamp".equals(classTypeName)){
                    Timestamp timestamp = (Timestamp) entry.getValue();
                    jsonObject.addProperty(key,timestamp.getTime());
                }
                else{
                    jsonObject.addProperty(key, entry.getValue()+"");
                }
            }
        }
        return jsonObject;
    }


}

