package com.common.redis;

import com.common.base.BaseDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Created by jianghaoming on 2017/11/22.
 */
@Component
@ConfigurationProperties(prefix="spring.redis")
public class RedisProperties extends BaseDto {

    private String password = "";

    private String host = "127.0.0.1";

    private String port = "6379";

    private String applicationName = "";

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
