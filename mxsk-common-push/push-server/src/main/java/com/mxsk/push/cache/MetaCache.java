package com.mxsk.push.cache;

import java.util.Collection;

/**
 * @author: zhengguican
 * create in 2021/5/12 13:57
 */
public interface MetaCache<T> {

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    T get(String key);

    /**
     * 设置缓存
     *
     * @param key   key
     * @param value 数据
     */
    void put(String key, T value);

    /**
     * 删除缓存
     *
     * @param key
     */
    void remove(String key);

    /**
     * 返回当前Key 集合
     *
     * @return
     */
    Collection<String> keySet();

    /**
     * 返回当前Value集合
     *
     * @return
     */
    Collection<T> values();
}
