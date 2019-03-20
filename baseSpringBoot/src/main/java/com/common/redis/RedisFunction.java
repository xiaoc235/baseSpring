package com.common.redis;

/**
 * redis方法
 * @author jianghaoming
 * @date 2018-12-25 17:20:47
 */
public class RedisFunction {

    @FunctionalInterface
    public interface GetByTypeToken<T>{
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface GetByString{
        String get() throws Exception;
    }

}
