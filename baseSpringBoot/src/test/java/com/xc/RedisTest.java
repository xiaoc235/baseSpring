package com.xc;

import com.common.redis.BaseCacheEntity;
import com.common.redis.RedisClient;
import com.common.redis.MyRedisProperties;
import com.common.spring.utils.CommonUtils;
import com.common.utils.DateFormatUtils;
import com.common.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RedisTest {

    private static final Logger log = LoggerFactory.getLogger(RedisTest.class);

    public static final TypeToken<TestUser> TYPE_TOKEN = new TypeToken<TestUser>(){};

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

        @Override
        public String toString() {
            return GsonUtils.toJson(this);
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
        MyRedisProperties redisProperties = new MyRedisProperties();
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
        MyRedisProperties redisProperties = new MyRedisProperties();
        redis.setRedisProperties(redisProperties);
        RedisEntity redisEntity = RedisEntity.instance;
        String result = redis.getAndSave(redisEntity, () -> "this a testaaa");
        System.out.println("result:" + result);
        System.out.println("cacheKey: " + redisEntity.getCacheKey());
        System.out.println("cacheValue: " + redis.get(redisEntity.getCacheKey()));
    }


    public class ApiConfigCountCacheEntity extends BaseCacheEntity {
        public ApiConfigCountCacheEntity(long configId){
            super();
            //次数有效期24小时
            setCacheTime(3600 * 24);
            //缓存key： 日期 + 数据库id
            setCacheKey("api_count_" + DateFormatUtils.getCurrentDate() + "_" + configId);
        }
    }

    RedisClient redis = new RedisClient();
    MyRedisProperties redisProperties = new MyRedisProperties();

    private void get(){
        redis.setRedisProperties(redisProperties);
        ApiConfigCountCacheEntity cacheEntity = new ApiConfigCountCacheEntity(1);
        String cacheKey = cacheEntity.getCacheKey();
        String countStr = redis.get(cacheKey);
        int count = CommonUtils.isBlank(countStr) ? 0 : Integer.parseInt(countStr);
        if(count <= 5){
            //使用api
            count++;
            log.info("{} 当前已经使用:{}", 1, count);
            redis.set(cacheKey, count+"");
        }
    }


    @Test
    public void testFor(){
        for(int i=0; i<10; i++){
            get();
        }
    }


    @Test
    public void testQueue(){
        redis.setRedisProperties(redisProperties);
        String cacheKey = "test_queue";
        redis.addQueue(cacheKey,"a");
        redis.addQueue(cacheKey,"b");
        redis.addQueue(cacheKey,"c");
        redis.addQueue(cacheKey,"1");
        redis.addQueue(cacheKey,"3");
        redis.addQueue(cacheKey,"2");
        log.info("当前队列长度:{}", redis.getQueueLength(cacheKey));
        log.info("当前队列数据:{}", redis.getQueueList(cacheKey));
        log.info("取出前三个");
        for(int i=0; i<3; i++){
            redis.popQueue(cacheKey);
        }
        log.info("当前队列长度:{}", redis.getQueueLength(cacheKey));
        log.info("当前队列数据:{}", redis.getQueueList(cacheKey));
    }

    @Test
    public void testQueueObj(){
        redis.setRedisProperties(redisProperties);
        String cacheKey = "test_queue_obj";
        for(int i=1; i<=6; i++) {
            redis.addQueue(cacheKey, new TestUser("name" + i, i));
        }

        log.info("当前队列长度:{}", redis.getQueueLength(cacheKey));
        log.info("当前队列数据:{}", redis.getQueueList(cacheKey));
        log.info("取出前三个");
        for(int i=0; i<3; i++){
            redis.popQueue(cacheKey);
        }
        log.info("当前队列长度:{}", redis.getQueueLength(cacheKey));
        log.info("当前队列数据:{}", redis.getQueueList(cacheKey));
    }

    @Test
    public void testNumber(){
        redis.setRedisProperties(redisProperties);
        String cacheKey = "test_int";

        redis.set(cacheKey, 54,600);

        long number = redis.getIncr(cacheKey);
        log.info("数值:{}", number);
        number = redis.getIncr(cacheKey);
        log.info("数值:{}", number);
        number = redis.getDecr(cacheKey);
        log.info("数值:{}", number);
    }


    @Test
    public void testMap(){
        redis.setRedisProperties(redisProperties);
        Map<String,String> map = new HashMap<>();
        String cacheKey = "maptest";
        for(int i=0; i<9; i++){
            redis.getCommand().hsetnx(cacheKey, "key" + i, "value" + i);
        }
        redis.getCommand().hsetnx(cacheKey, "key2" , "value2222");
        Map<String,String> resultMap = redis.getCommand().hgetall(cacheKey);
        for(Map.Entry<String,String> entry : resultMap.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    @Test
    public void testMap1(){
        redis.setRedisProperties(redisProperties);
        String cacheKey = "maptest1";
        for(int i=0; i<=9; i++){
           redis.addMap(cacheKey, "key" + i, "value" + i);
        }
        String value = redis.getMapValue(cacheKey, "key6");
        System.out.println("单独key:" + value);
        Map<String,String> resultMap = redis.getMap(cacheKey);
        System.out.println(resultMap);
    }

    @Test
    public void testMap2(){
        redis.setRedisProperties(redisProperties);
        Map<String,TestUser> map = new HashMap<>();
        String cacheKey = "maptest2";
        for(int i=0; i<=9; i++){
            map.put("key" + i, new TestUser("key"+i, i));
        }
        redis.addMap(cacheKey, map);
        TestUser value = redis.getMapValue(cacheKey, "key6", TYPE_TOKEN);
        System.out.println("单独key:" + value);
        Map<String,String> resultMap = redis.getMap(cacheKey);
        System.out.println(resultMap);
    }
}
