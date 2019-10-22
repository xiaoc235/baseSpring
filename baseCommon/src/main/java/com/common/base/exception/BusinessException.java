package com.common.base.exception;

/**
 * 业务异常
 */

public class BusinessException extends Exception {


    private int errorCode = 200;

    private String errorDesc ="";

    public BusinessException(){
        super();
    }

    public BusinessException(int code, String message) {
        super("code: "+ code + ", message: " + message);
        this.errorCode = code;
        this.errorDesc = message;
    }

    public BusinessException(String message) {
        this(1,message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    @Override
    public String toString() {
    	return "errorCode:" + this.errorCode + ";  errorDesc:" + this.errorDesc;
    }

}
