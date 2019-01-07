package com.common.redis;

import com.common.base.BaseDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Created by jianghaoming on 2017/11/22.
 */
@Component
@ConfigurationProperties(prefix="spring.redis")
public class RedisProperties extends org.springframework.boot.autoconfigure.data.redis.RedisProperties {

    private String applicationName = "";

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
