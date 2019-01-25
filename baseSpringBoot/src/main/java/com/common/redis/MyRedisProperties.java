package com.common.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Created by jianghaoming on 2017/11/22.
 */
@Component("myRedisProperties")
@ConfigurationProperties(prefix = "spring.redis")
public class MyRedisProperties extends RedisProperties {

    private String applicationName = "";

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
