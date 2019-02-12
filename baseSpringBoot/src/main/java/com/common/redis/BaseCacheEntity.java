package com.common.redis;

/**
 * @author edianzu
 */
public abstract class BaseCacheEntity {

    private String cacheKey = this.getClass().getSimpleName();

    private int cacheTime = -1;

    public BaseCacheEntity(){}

    public BaseCacheEntity(String cacheKey){
        this.cacheKey = cacheKey;
    }

    public BaseCacheEntity(String cacheKey, int cacheTime){
        this.cacheKey = cacheKey;
        this.cacheTime = cacheTime;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public void setCacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public int getCacheTime() {
        return cacheTime;
    }
}
