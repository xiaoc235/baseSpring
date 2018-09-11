package com.common.base.exception;

/**
 * 业务异常
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = -4117711143814628555L;

    private int errorCode = 1;

    private String errorDesc ="";

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable t) {
        super(t);
    }

    public BusinessException(int code, String message, Throwable t) {
        super(t);
        this.errorCode = code;
        this.errorDesc = message;
    }

    public BusinessException(int code, String message) {
        this(code, message, new Exception("errorCode:" + code + ";errorDesc: " + message));
    }

    public BusinessException(String message) {
        this(1, message);
    }

    public BusinessException(String message, Throwable t) {
        this(1, message, t);
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
    	return "errorCode:" + this.errorCode + ";errorDesc:" + this.errorDesc;
    }

}
