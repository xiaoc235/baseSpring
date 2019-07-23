/**
 * update jhm 20180721
 * jedis --> luttuce
 *
 * update by jhm 20190723 :
 * support cluster & pool
 */

package com.common.redis.client;



import com.common.redis.MyRedisProperties;
import com.common.utils.GsonUtils;
import io.lettuce.core.RedisURI;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.util.ObjectUtils;

/**
 * 基本实现
 * @author jianghaoming
 * @date 2019-07-23 17:11:48
 */
public class BaseRedisClientImpl {

	static MyRedisProperties myRedisProperties;

    @Autowired
	public void setRedisProperties(MyRedisProperties myRedisProperties) {
		BaseRedisClientImpl.myRedisProperties = myRedisProperties;
	}

	public static MyRedisProperties getMyRedisProperties(){
        return myRedisProperties;
    }

	protected static GenericObjectPoolConfig getPoolConfig(){
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        RedisProperties.Pool poolProperties = myRedisProperties.getLettuce().getPool();
        poolConfig.setMaxIdle(poolProperties.getMaxIdle());
        poolConfig.setMaxTotal(poolProperties.getMaxActive());
        poolConfig.setMinIdle(poolProperties.getMinIdle());
        poolConfig.setMaxWaitMillis(poolProperties.getMaxWait().toMillis());
        return poolConfig;
    }

    protected static RedisURI makeRedisURI(String host, int port, String passwd){
        RedisURI uri = new RedisURI();
        uri.setHost(host);
        uri.setPort(port);
        if (!ObjectUtils.isEmpty(passwd)) {
            uri.setPassword(passwd);
        }
        return uri;
    }

    protected String buildKey(String key){
        return myRedisProperties.getApplicationName() + "_"  + key;
    }

    protected <T> String buildValue(T value){
        return value instanceof String ? value.toString() : GsonUtils.toJson(value);
    }





}