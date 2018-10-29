package com.common.base.model;

import java.util.List;

/**
 * Created by jianghaoming on 17/3/16.
 */
public class MyPageResult<T> {

    //总共多少条
    private long totalCount;

    //总共多少页
    private int totalPageNumber;

    //结果
    private List<T> resultList;


    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
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
