package com.common.base.exception;

import com.common.base.CommConstants;

/**
 * 未找到相关信息 异常
 * @author jianghaoming
 * @date 2019-03-07 11:38:29
 */
public class NotFundException extends BusinessException {

    private static final int ERROR_CODE = 404;
    public NotFundException(){
        super(ERROR_CODE, CommConstants.NOT_FUND);
    }

    public NotFundException(String msg){
        super(ERROR_CODE, CommConstants.NOT_FUND + " " +msg);
    }
}
