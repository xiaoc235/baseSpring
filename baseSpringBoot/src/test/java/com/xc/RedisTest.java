package com.xc;

import com.common.redis.RedisClient;
import com.common.redis.RedisProperties;
import com.common.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.UUID;

public class RedisTest {



    public class TestUser implements Serializable {
        private String name;
        private Integer age;

        public TestUser(String _name, int _age){
            this.name = _name;
            this.age = _age;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    private static StatefulRedisConnection<String, String> connection = null;

    private static synchronized void initRedis(){
        System.out.println("初始化redis");
        String passwd = "";
        String host = "127.0.0.1";
        String port = "6379";
        if(ObjectUtils.isEmpty(connection)){
            RedisURI uri = new RedisURI();
            uri.setHost(host);
            uri.setPort(Integer.parseInt(port));
            if(!ObjectUtils.isEmpty(passwd)) {
                uri.setPassword(passwd);
            }
            io.lettuce.core.RedisClient lettuceRedis = io.lettuce.core.RedisClient.create(uri);
            connection = lettuceRedis.connect();
        }
    }

    public RedisCommands<String, String> getCommand(){
        if(ObjectUtils.isEmpty(connection)) {
            initRedis();
        }
        return connection.sync();
    }

    @Test
    public void test1(){
        int number = 99999;
        long setT1 = System.currentTimeMillis();
        for(int i=0; i<number; i++) {
            TestUser user = new TestUser(UUID.randomUUID().toString(), i);
            getCommand().set("i" + i, GsonUtils.toJson(user));
        }
        long setT2 = System.currentTimeMillis();
        System.out.println("写入总用时:" + (setT2 - setT1));

        long getT1 = System.currentTimeMillis();
        for(int i=0; i<number; i++) {
            System.out.println(getCommand().get("i" + i));
        }
        long getT2 = System.currentTimeMillis();
        System.out.println("查询总用时:" + (getT2 - getT1));
        System.out.println("总用时:" + (getT2 - setT1));
    }


    @Test
    public void testRedisFunction() throws Exception {
        RedisClient redis = new RedisClient();
        RedisProperties redisProperties = new RedisProperties();
        redis.setRedisProperties(redisProperties);
        String key = "t_function";
        String result = redis.get(key, () ->{
            String value = "test";
            System.out.println("没有缓存");
            redis.set(key, value);
            return value;
        });
        System.out.println("result:" + result);


        String key2 = "t_function_user";
        TestUser resultUser = redis.get(key2, new TypeToken<TestUser>(){}, () -> {
            TestUser value = new TestUser("name",10);
            System.out.println("no entity cache");
            redis.set(key2, value);
            return value;
        });
        System.out.println(GsonUtils.toJson(resultUser));

    }

    @Test
    public void testGetAndSet() throws Exception {
        RedisClient redis = new RedisClient();
        RedisProperties redisProperties = new RedisProperties();
        redis.setRedisProperties(redisProperties);
        RedisEntity redisEntity = RedisEntity.instance;
        String result = redis.getAndSave(redisEntity, () -> "this a testaaa");
        System.out.println("result:" + result);
        System.out.println("cacheKey: " + redisEntity.getCacheKey());
        System.out.println("cacheValue: " + redis.get(redisEntity.getCacheKey()));
    }

}
