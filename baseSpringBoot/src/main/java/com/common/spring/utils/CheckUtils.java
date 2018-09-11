package com.common.spring.utils;

import com.common.base.exception.BusinessException;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jianghaoming on 17/2/27.
 */
public class CheckUtils {

    private CheckUtils(){
        super();
    }

    public static void checkParamNull(Object object, String message) throws BusinessException {
        if(CommonUtils.isBlank(object)){
            throw new BusinessException(message);
        }
    }

    public static void checkMoible(final String mobile) throws BusinessException{
        checkParamNull(mobile,"手机号码不能为空");
        if(!CommonUtils.isMobile(mobile)){
            throw new BusinessException("请输入正确的手机号码");
        }
    }



}
