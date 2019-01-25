package com.common.base;


import com.common.utils.GsonUtils;

import java.io.Serializable;

/**
 *
 * @author jianghaoming
 * @date 17/2/24
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 2935363275944294319L;

    public BaseEntity(){
        super();
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }




}
