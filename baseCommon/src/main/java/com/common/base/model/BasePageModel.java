package com.common.base.model;


import com.common.base.BaseDto;
import com.common.base.annotation.CanNullAnnotation;

/**
 * Created by jianghaoming on 17/2/24.
 *
 *  基本的数据请求model
 */
public class BasePageModel extends BaseDto {

    @CanNullAnnotation
    private int page = 0;

    @CanNullAnnotation
    private int size = 10;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
