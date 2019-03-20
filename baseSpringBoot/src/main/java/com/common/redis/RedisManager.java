package com.common.redis;

import com.common.spring.ToolSpring;
import com.google.gson.reflect.TypeToken;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.List;

/**
 * Created by jianghaoming on 2016/12/29.17:52
 */
public class RedisManager {

    public static final TypeToken<List<String>>  LIST_STR_TYPE_TOKEN = new TypeToken<List<String>>(){};

    private RedisClient getRedisClient() {
        return (RedisClient) ToolSpring.getBean("RedisClient");
    }

    private static RedisManager instance = null;

    static {
        instance = new RedisManager();
    }

    private RedisManager() {}
    private static RedisManager getInstance() {
        return instance;
    }

    public static RedisClient getRedis(){
        return getInstance().getRedisClient();
    }
    public static RedisCommands<String,String> getCommand(){
        return getRedis().getCommand();
    }






}
