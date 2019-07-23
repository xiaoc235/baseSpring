package com.common.spring;

import com.common.base.CommConstants;
import com.common.base.exception.BusinessException;
import com.common.redis.client.RedisClient;
import com.common.spring.utils.CommonUtils;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;


/**
 * 对redis 有依赖
 * @author jianghaoming
 * @date 2019-02-12 15:04:48
 */
public class TokenAuth {

    private HttpServletRequest request;
    private RedisClient redis;

    public TokenAuth(HttpServletRequest httpServletRequest, RedisClient redisClient){
        request = httpServletRequest;
        redis = redisClient;
    }

    public <T> T getUserAndVerifyToken(final String token,  TypeToken<T> typeToken) throws BusinessException{
        if(CommonUtils.isBlank(token)){
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), CommConstants.LOGIN_OUT_MESSAGE);
        }
        T user = redis.get(token, typeToken);
        if(null == user){
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(),CommConstants.LOGIN_OUT_MESSAGE);
        }
        return user;
    }

    public String getToken(){
        return getRequest().getHeader("token");
    }

    public HttpServletRequest getRequest(){
        return request;
    }

}
