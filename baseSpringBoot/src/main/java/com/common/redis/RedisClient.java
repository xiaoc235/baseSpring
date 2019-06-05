package com.common.redis;

import com.common.base.exception.BusinessException;
import com.common.spring.utils.CommonUtils;
import com.common.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;


/**
 *
 * update jhm 20180721
 * jedis --> luttuce
 *
 * get --> sync
 * set & del --> async
 */
@Component("RedisClient")
public class RedisClient {


	private static MyRedisProperties myRedisProperties;

	@Autowired
	public void setRedisProperties(MyRedisProperties myRedisProperties) {
		RedisClient.myRedisProperties = myRedisProperties;
	}

    private static StatefulRedisConnection<String, String> connection = null;
    private static io.lettuce.core.RedisClient lettuceRedis = null;

	private static synchronized void initRedis(){
		String passwd = myRedisProperties.getPassword();
		String host = myRedisProperties.getHost();
		int port = myRedisProperties.getPort();
        if(ObjectUtils.isEmpty(lettuceRedis)){
            RedisURI uri = new RedisURI();
            uri.setHost(host);
            uri.setPort(port);
            if(!ObjectUtils.isEmpty(passwd)) {
                uri.setPassword(passwd);
            }
            lettuceRedis = io.lettuce.core.RedisClient.create(uri);
            connection = lettuceRedis.connect();
        }
	}

    /**
     * 异步
     * @return
     */
	public RedisAsyncCommands<String, String> getAsyncCommand(){
	    if(ObjectUtils.isEmpty(lettuceRedis)){
	        initRedis();
        }
		if(ObjectUtils.isEmpty(connection)) {
            connection = lettuceRedis.connect();
		}
		return connection.async();
	}

    /**
     * 同步
     * @return
     */
    public RedisCommands<String, String> getCommand(){
        if(ObjectUtils.isEmpty(lettuceRedis)){
            initRedis();
        }
        if(ObjectUtils.isEmpty(connection)) {
            connection = lettuceRedis.connect();
        }
        return connection.sync();
    }

	public void closeConnection(){
		if(ObjectUtils.isEmpty(connection)){
            connection.close();
		}
	}

	/**
	 * 存入 String
	 */

    public void set(String key, String value){
        this.set(key,value,-1);
    }

	public void set(String key, String value, int seconds){
        if(seconds > 0) {
            getAsyncCommand().setex(this.buildKey(key), seconds, value);
        }else{
            getAsyncCommand().set(this.buildKey(key),value);
        }
	}

	/**
	 * 存储对象
	 * @param key
	 */
	public <T> void set(String key, T t){
	    this.set(key,t,-1);
	}
	public <T> void set(String key, T t, int seconds){
	    try {
	        String value = "";
	        if(t instanceof java.lang.String){
                value = t.toString();
            }else if(t instanceof java.lang.Number){
	            value = t.toString();
            }else if(t instanceof java.lang.Boolean){
	            value = t.toString();
            }else{
                value = GsonUtils.toJson(t);
            }
            this.set(key,value,seconds);
        }catch (Exception e){
	        //不能json转换，直接强转为string
            this.set(key, t + "", seconds);
        }

    }

	/**
	 * 读取 String
	 * @param key
	 * @return String
	 */
	public String get(String key){
	    if(this.exists(key)) {
           return getCommand().get(buildKey(key));
        }
		return "";
	}

    public String get(String key, RedisFunction.GetByString function) throws Exception {
        if(this.exists(key)){
            return this.get(key);
        }
        return function.get();
    }

    /**
     * 获取int类型值,默认为0
     * @param key
     * @return
     */
    public int getInt(String key){
	    key = buildKey(key);
	    if(!exists(key)){
	        return 0;
        }
	    return Integer.parseInt(this.get(key));
    }

    /**
     * 整数递增，默认值为0，第一次获取为1
     * @param key
     * @return
     */
    public long getIncr(String key){
        return getCommand().incr(buildKey(key));
    }

    /**
     * 整数递减，默认值为0，第一次获取为-1
     * @param key
     * @return
     */
    public long getDecr(String key){
        return getCommand().decr(buildKey(key));
    }

    /**
     * 读取并保存
     * @author jianghaoming
     * @date 2019-01-07 14:54:37
     */
    public String getAndSave(BaseCacheEntity entity, RedisFunction.GetByString function) throws Exception {
        String value = this.get(entity.getCacheKey(), function);
        if(!CommonUtils.isBlank(value)) {
            this.set(entity.getCacheKey(), value, entity.getCacheTime());
        }
	    return value;
    }


    /**
	 * 读取 对象
	 * @param key
	 * @return object
	 */
	public <T> T get(String key, TypeToken<T> typeToken){
	    if(this.exists(key)){
            String json = get(key);
            return  GsonUtils.conver(json, typeToken);
        }
		return null;
	}

    public <T> T get(String key, TypeToken<T> typeToken, RedisFunction.GetByTypeToken<T> function) throws Exception {
        T result = this.get(key,typeToken);
        if(CommonUtils.isBlank(result)){
            return function.get();
        }
        return result;
    }

    /**
     * 读取并保存
     * @author jianghaoming
     * @date 2019-01-07 14:54:37
     */
    public <T> T getAndSave(BaseCacheEntity entity, TypeToken<T> typeToken, RedisFunction.GetByTypeToken<T> function) throws Exception {
        T value = this.get(entity.getCacheKey(),typeToken, function);
        if(value != null) {
            this.set(entity.getCacheKey(), value, entity.getCacheTime());
        }
        return value;
    }


	public Boolean exists(String key){
        key = buildKey(key);
		return getCommand().exists(key) > 0;
	}

	/**
	 * @Title: 检查是否为空
	 * @author jianghaoming
	 * @date 2017/1/13  14:33
	 */
	public void checkNull(String key) throws BusinessException {
		if(!exists(key)){
			throw new BusinessException("未找到相关redis数据，key :" + key);
		}
	}

	/**
	 * @Title: 获取key的有效期
	 * @author jianghaoming
	 * @date 2017/1/13  14:29
	 * @return 单位为秒 , -1, 永不过期。
	 */
	public int getTtl(String key) throws BusinessException {
		checkNull(key);
		return Math.toIntExact(getCommand().ttl(buildKey(key)));
	}

    /**
     * 获取key值列表
     */
	public List<String> getKeys(String pattern){
        return getCommand().keys(buildKey(pattern));
    }

	/**
	 * 删除
	 */
	public void delKey(String key){
		getAsyncCommand().del(buildKey(key));
	}

	public void delKeys(String pattern){
        List<String> result = getKeys(pattern);
        for(String key : result){
            delKey(key);
        }
    }


    // =============================   队列操作 ============================================
    /**
     * 取出队列
     * @param key
     */
    public <T> T popQueue(String key, TypeToken<T> typeToken){
        String value = popQueue(key);
        return GsonUtils.conver(value, typeToken);
    }
    public String popQueue(String key){
        return getCommand().rpop(buildKey(key));
    }

    /**
     * 获取队列所有数据， 索引从0开始
     * @param key
     * @param l 最左边索引
     * @param r 最右边索引
     * @return
     */
    public List<String> getQueueList(String key, int l, int r){
        List<String> list = getCommand().lrange(buildKey(key),l, r);
        Collections.reverse(list);
        return list;
    }

    public List<String> getQueueList(String key){
        return getQueueList(key,0,-1);
    }

    public <T> List<T> getQueueList(String key, TypeToken<T> typeToken){
        List<String> list = getQueueList(key);
        List<T> resultList = new LinkedList<>();
        list.forEach(jsonStr ->
            resultList.add(GsonUtils.conver(jsonStr, typeToken))
        );
        return resultList;
    }

    /**
     * 获取队列长度
     * @param key
     * @return
     */
    public long getQueueLength(String key){
        return getCommand().llen(buildKey(key));
    }


    /**
     * 添加到队列
     */
    public <T> void addQueue(String key, T t){
        String value = t instanceof java.lang.String ? t.toString() : GsonUtils.toJson(t);
        getAsyncCommand().lpush(buildKey(key),value);
    }


    //================  map操作 =================

    /**
     * 获取map
     * @param key
     * @return
     */
    public Map<String, String> getMap(String key){
        return getCommand().hgetall(buildKey(key));
    }

    public String getMapValue(String key, String mapKey){
        return getCommand().hget(buildKey(key),mapKey);
    }

    public <T> Map<String, T> getMap(String key, TypeToken<T> typeToken){
        Map<String,String> result = this.getMap(key);
        Map<String, T> map = new HashMap<>();
        for(Map.Entry<String,String> entry : result.entrySet()){
            map.put(entry.getKey(), GsonUtils.conver(entry.getValue(), typeToken));
        }
        return map;
    }

    public <T> T getMapValue(String key, String mapKey,  TypeToken<T> typeToken){
        String result = this.getMapValue(key, mapKey);
        return GsonUtils.conver(result, typeToken);
    }


    /**
     * 添加值进map ， mapKey存在则更新
     * @param key
     * @param mapKey
     * @param mapValue
     */
    public <T> void addMap(String key, String mapKey, T mapValue){
        getAsyncCommand().hset(buildKey(key), mapKey, buildValue(mapValue));
    }

    /**
     * 添加map
     * @param key
     */
    public <T> void addMap(String key, Map<String,T> map){
        for(Map.Entry<String,T> entry : map.entrySet()){
            getAsyncCommand().hset(buildKey(key), entry.getKey(), buildValue(entry.getValue()));
        }
    }

    /**
     * 添加值进map ， 仅mapKey不存在时执行操作
     * @param key
     * @param mapKey
     * @param mapValue
     */
    public <T> void addMapNx(String key, String mapKey, T mapValue){
        getAsyncCommand().hsetnx(key, mapKey, buildValue(mapValue));
    }


    /**
	 * 清空DB
	 */
	 public void flushDB(){
		 getCommand().flushdb();
	 }
	 
	/**
	 * 清空所有
	 */
	 public void flushAll(){
		getCommand().flushall();
	 }

	 private String buildKey(String key){
	     return myRedisProperties.getApplicationName() + "_"  + key;
     }

     private <T> String buildValue(T value){
         return value instanceof String ? value.toString() : GsonUtils.toJson(value);
     }

	
}