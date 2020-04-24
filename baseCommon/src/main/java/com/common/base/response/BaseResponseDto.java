package com.common.base.response;

import com.common.base.BaseEntity;


/**
 * @param <T>
 */
public class BaseResponseDto<T> extends BaseEntity {

    private Boolean isSuccess = false;
    private int sysCode;
    private int businessCode;
    private String sysMessage;
    private T returnObj;

    public BaseResponseDto( Boolean success , int code, String message, T data) {
        super();
        this.isSuccess = success;
        this.sysCode = code;
        this.businessCode = code;
        this.sysMessage = message;
        if(data != null) {
            this.returnObj = data;
        }
    }

    public BaseResponseDto() {
        super();
    }


    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public int getSysCode() {
        return sysCode;
    }

    public void setSysCode(int sysCode) {
        this.sysCode = sysCode;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

    public String getSysMessage() {
        return sysMessage;
    }

    public void setSysMessage(String sysMessage) {
        this.sysMessage = sysMessage;
    }

    public T getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(T returnObj) {
        this.returnObj = returnObj;
    }
}
