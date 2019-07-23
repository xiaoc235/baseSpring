package com.xc;

import com.common.redis.client.RedisClient;
import com.common.redis.MyRedisProperties;
import com.common.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

public class SerializeTest implements Serializable{


    public class TestUser implements Serializable{
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








    @Test
    public void testObj(){
        TestUser user = new TestUser("test1",1);
        final String testkey = "t_k_obj";
        RedisClient redis = new RedisClient();
        redis.setRedisProperties(new MyRedisProperties());
        long currentTime = System.currentTimeMillis();
        redis.set(testkey,user);
        System.out.println(System.currentTimeMillis() - currentTime);

        currentTime = System.currentTimeMillis();
        TypeToken<TestUser> typeToken = new TypeToken<TestUser>(){};
        TestUser user1 =  redis.get(testkey, typeToken);
        System.out.println(System.currentTimeMillis() - currentTime);

        System.out.println(GsonUtils.toJson(user1));
    }

    @Test
    public void testList(){

        final String testkey = "t_k_list";
        RedisClient redis = new RedisClient();
        redis.setRedisProperties(new MyRedisProperties());

        List<TestUser> list = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        String name = UUID.randomUUID().toString();
        for(int i=0; i<10; i++) {
            TestUser user = new TestUser(name, 99+i);
            list.add(user);
        }
        System.out.println(System.currentTimeMillis() - currentTime);

        currentTime = System.currentTimeMillis();
        redis.set(testkey,list);
        System.out.println(System.currentTimeMillis() - currentTime);

        currentTime = System.currentTimeMillis();
        TypeToken<List<TestUser>> typeToken = new TypeToken<List<TestUser>>(){};
        List<TestUser> userList = redis.get(testkey, typeToken);
        System.out.println(System.currentTimeMillis() - currentTime);
        for(TestUser user : userList){
            System.out.println(GsonUtils.toJson(user));
        }
    }

    @Test
    public void testMap(){
        final String testkey = "t_k_map";
        RedisClient redis = new RedisClient();
        redis.setRedisProperties(new MyRedisProperties());

        Map<String,TestUser> map = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        String name = UUID.randomUUID().toString();
        for(int i=0; i<10; i++) {
            TestUser user = new TestUser(name, 99+i);
            map.put(i+"",user);
        }
        System.out.println(System.currentTimeMillis() - currentTime);

        currentTime = System.currentTimeMillis();
        redis.set(testkey,map);
        System.out.println(System.currentTimeMillis() - currentTime);

        currentTime = System.currentTimeMillis();

        Map<String,TestUser> userMap = redis.get(testkey,new TypeToken<Map<String,TestUser>>(){});
        //Map<String,TestUser> userMap = redis.get(testkey, new MapGsonTypeToken<String,TestUser>());
        System.out.println(System.currentTimeMillis() - currentTime);

        for(Map.Entry<String,TestUser> entry : userMap.entrySet()){
            System.out.println(entry.getKey()+" - "+ GsonUtils.toJson(entry.getValue()));
        }

    }
}
