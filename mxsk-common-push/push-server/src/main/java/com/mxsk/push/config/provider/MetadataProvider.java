package com.mxsk.push.config.provider;


import com.mxsk.push.cache.AbstractMetadata;

import java.util.Collection;

/**
 * 获取数据源配置清单
 *
 * @author evan
 * create-at 2021/3/25
 */
public interface MetadataProvider {

    /**
     * 获取源配置信息
     *
     * @return 数据源配置信息
     */
    Collection<AbstractMetadata> get();
}
