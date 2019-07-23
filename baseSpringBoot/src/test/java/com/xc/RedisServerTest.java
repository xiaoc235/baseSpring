package com.xc;

import com.common.redis.client.RedisClient;
import com.common.redis.client.RedisClusterClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    public void testRedis(){
        redisClient.set("test3", "333333");
        redisClient.set("test4", "444444");
        System.out.println(redisClient.get("test3"));
        System.out.println(redisClient.get("test4"));
    }

}
