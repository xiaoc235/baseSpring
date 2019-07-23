/**
 * update jhm 20180721
 * jedis --> luttuce
 *
 * update by jhm 20190723 :
 * support cluster & pool
 */

package com.common.redis.client;



import com.common.base.exception.BusinessException;
import com.common.redis.BaseCacheEntity;
import com.common.redis.RedisFunction;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;



public interface BaseRedisClient {

	/**
	 * 存入 String
	 */
    void set(String key, String value);

	void set(String key, String value, int seconds);
	/**
	 * 存储对象
	 * @param key
	 */
	 <T> void set(String key, T t);
	<T> void set(String key, T t, int seconds);

	/**
	 * 读取 String
	 * @param key
	 * @return String
	 */
	String get(String key);

    String get(String key, RedisFunction.GetByString function) throws Exception;

    /**
     * 获取int类型值,默认为0
     * @param key
     * @return
     */
    int getInt(String key);

    /**
     * 整数递增，默认值为0，第一次获取为1
     * @param key
     * @return
     */
    long getIncr(String key);

    /**
     * 整数递减，默认值为0，第一次获取为-1
     * @param key
     * @return
     */
    long getDecr(String key);

    /**
     * 读取并保存
     * @author jianghaoming
     * @date 2019-01-07 14:54:37
     */
    String getAndSave(BaseCacheEntity entity, RedisFunction.GetByString function) throws Exception;


    /**
	 * 读取 对象
	 * @param key
	 * @return object
	 */
	<T> T get(String key, TypeToken<T> typeToken);

     <T> T get(String key, TypeToken<T> typeToken, RedisFunction.GetByTypeToken<T> function) throws Exception;

    /**
     * 读取并保存
     * @author jianghaoming
     * @date 2019-01-07 14:54:37
     */
     <T> T getAndSave(BaseCacheEntity entity, TypeToken<T> typeToken, RedisFunction.GetByTypeToken<T> function) throws Exception;


	Boolean exists(String key);

	/**
	 * @Title: 检查是否为空
	 * @author jianghaoming
	 * @date 2017/1/13  14:33
	 */
	void checkNull(String key) throws BusinessException;

	/**
	 * @Title: 获取key的有效期
	 * @author jianghaoming
	 * @date 2017/1/13  14:29
	 * @return 单位为秒 , -1, 永不过期。
	 */
	int getTtl(String key) throws BusinessException;

    /**
     * 获取key值列表
     */
	List<String> getKeys(String pattern);

	/**
	 * 删除
	 */
	void delKey(String key);

	void delKeys(String pattern);


    // =============================   队列操作 ============================================
    /**
     * 取出队列
     * @param key
     */
     <T> T popQueue(String key, TypeToken<T> typeToken);
    String popQueue(String key);

    /**
     * 获取队列所有数据， 索引从0开始
     * @param key
     * @param l 最左边索引
     * @param r 最右边索引
     * @return
     */
    List<String> getQueueList(String key, int l, int r);

    List<String> getQueueList(String key);
    <T> List<T> getQueueList(String key, TypeToken<T> typeToken);

    /**
     * 获取队列长度
     * @param key
     * @return
     */
    long getQueueLength(String key);


    /**
     * 添加到队列
     */
    <T> void addQueue(String key, T t);


    //================  map操作 =================

    /**
     * 获取map
     * @param key
     * @return
     */
    Map<String, String> getMap(String key);

    String getMapValue(String key, String mapKey);

    <T> Map<String, T> getMap(String key, TypeToken<T> typeToken);

    <T> T getMapValue(String key, String mapKey,  TypeToken<T> typeToken);


    /**
     * 添加值进map ， mapKey存在则更新
     * @param key
     * @param mapKey
     * @param mapValue
     */
    <T> void addMap(String key, String mapKey, T mapValue);
    /**
     * 添加map
     * @param key
     */
    <T> void addMap(String key, Map<String,T> map);

    /**
     * 添加值进map ， 仅mapKey不存在时执行操作
     * @param key
     * @param mapKey
     * @param mapValue
     */
    <T> void addMapNx(String key, String mapKey, T mapValue);


    /**
	 * 清空DB
	 */
	 void flushDB();
	 
	/**
	 * 清空所有
	 */
	 void flushAll();


	
}