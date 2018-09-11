package com.common.base.model;

import java.util.List;

/**
 * Created by jianghaoming on 17/3/16.
 */
public class MyPageResult<T> {

    //总共多少条
    private int totalCount;

    //总共多少页
    private int totalPageNumber;

    //排序
    private List<T> resultList;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPageNumber() {
        return totalPageNumber;
    }

    public void setTotalPageNumber(int totalPageNumber) {
        this.totalPageNumber = totalPageNumber;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}
