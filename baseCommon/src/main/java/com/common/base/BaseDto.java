package com.common.base;


import com.common.utils.GsonUtils;

import java.io.Serializable;

/**
 * Created by jianghaoming on 17/2/24.
 */
public class BaseDto implements Serializable {

    private static final long serialVersionUID = 2935363275944294319L;

    public BaseDto(){
        super();
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }




}
