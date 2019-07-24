package com.xc;

import com.common.redis.client.RedisClient;
import com.common.redis.client.RedisClusterClient;
import com.common.utils.GsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class RedisServerTest {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private RedisClusterClient redisClusterClient;

    @Test
    public void testCluster(){
        redisClusterClient.set("test1", "11111");
        redisClusterClient.set("test2", "22222");
        System.out.println(redisClusterClient.get("test1"));
        System.out.println(redisClusterClient.get("test2"));
    }

    @Test
    public void testCluster2(){
        int number = 100;
        long setT1 = System.currentTimeMillis();
        for(int i=0; i<number; i++) {
            RedisTest.TestUser user = new RedisTest.TestUser(UUID.randomUUID().toString(), i);
            redisClusterClient.set("i" + i, GsonUtils.toJson(user));
        }
        long setT2 = System.currentTimeMillis();
        System.out.println("写入总用时:" + (setT2 - setT1));

        long getT1 = System.currentTimeMillis();
        for(int i=0; i<number; i++) {
            String result = redisClusterClient.get("i" + i);
            //System.out.println(result);
        }
        long getT2 = System.currentTimeMillis();
        System.out.println("查询总用时:" + (getT2 - getT1));
        System.out.println("总用时:" + (getT2 - setT1) + ", 处理数据:" + number);
    }



    @Test
    public void testRedis(){
        redisClient.set("test3", "333333");
        redisClient.set("test4", "444444");
        System.out.println(redisClient.get("test3"));
        System.out.println(redisClient.get("test4"));
    }

    @Test
    public void testRedis2(){
        int number = 100_000;
        long setT1 = System.currentTimeMillis();
        for(int i=0; i<number; i++) {
            RedisTest.TestUser user = new RedisTest.TestUser(UUID.randomUUID().toString(), i);
            redisClient.set("i" + i, GsonUtils.toJson(user));
        }
        long setT2 = System.currentTimeMillis();
        System.out.println("写入总用时:" + (setT2 - setT1));

        long getT1 = System.currentTimeMillis();
        for(int i=0; i<number; i++) {
            String result = redisClient.get("i" + i);
            //System.out.println(result);
        }
        long getT2 = System.currentTimeMillis();
        System.out.println("查询总用时:" + (getT2 - getT1));
        System.out.println("总用时:" + (getT2 - setT1) + ", 处理数据:" + number);
    }

}
