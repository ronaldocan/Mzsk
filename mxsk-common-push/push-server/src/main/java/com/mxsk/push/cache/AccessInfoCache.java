package com.mxsk.push.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mxsk.push.config.provider.AccessTokenProvider;
import com.mxsk.push.config.provider.SmsDriverMetadataProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 短信平台账号源数据缓存
 *
 * @author: zhengguican
 * create in 2021/5/12 13:58
 */
@Slf4j
public class AccessInfoCache implements MetaCache<AccessInfoMeta> {

    private final ConcurrentMap<String, AccessInfoMeta> readOnlyCacheMap = new ConcurrentHashMap<String, AccessInfoMeta>(16);

    private final LoadingCache<String, AccessInfoMeta> readWriteCacheMap;

    private final ScheduledExecutorService executorService;

    private final AccessTokenProvider accessTokenProvider;

    public AccessInfoCache(SmsDriverMetadataProperties smsDriverMetadataProperties,
                           AccessTokenProvider accessTokenProvider) {

        executorService = Executors.newScheduledThreadPool(1);
        this.accessTokenProvider = accessTokenProvider;
        readWriteCacheMap = CacheBuilder.newBuilder().initialCapacity(16).
                expireAfterWrite(Long.valueOf(smsDriverMetadataProperties.getMetaExpireSeconds()), SECONDS)
                .build(new CacheLoader<String, AccessInfoMeta>() {
                    @Override
                    public AccessInfoMeta load(String key) throws Exception {
                        executorService.scheduleWithFixedDelay(new Runnable() {
                                                                   @Override
                                                                   public void run() {
                                                                       refresh();
                                                                   }
                                                               }, smsDriverMetadataProperties.getMetaFetchIntervalSeconds(),
                                smsDriverMetadataProperties.getMetaFetchIntervalSeconds(), TimeUnit.SECONDS);
                        return get(readOnlyCacheMap.keySet().iterator().next());
                    }
                });
        this.refresh();
    }

    @Override
    public AccessInfoMeta get(String key) {
        AccessInfoMeta payload = null;
        try {
            final AccessInfoMeta currentPayload = readOnlyCacheMap.get(key);
            if (currentPayload != null) {
                payload = currentPayload;
            } else {
                payload = readWriteCacheMap.get(key);
                readOnlyCacheMap.put(key, payload);
            }
        } catch (Throwable t) {
            log.error("Cannot get value for key : {}", key, t);
        }
        return payload;
    }

    @Override
    public void put(String key, AccessInfoMeta value) {

    }


    @Override
    public void remove(String key) {

        if (readOnlyCacheMap.containsKey(key)) {
            readOnlyCacheMap.remove(key);
        }
        if (readWriteCacheMap.asMap().get(key) != null) {
            readWriteCacheMap.invalidate(key);
        }
    }

    @Override
    public Collection<String> keySet() {
        return readOnlyCacheMap.keySet();
    }

    @Override
    public Collection<AccessInfoMeta> values() {
        return readOnlyCacheMap.values();
    }

    public void refresh() {
        Collection<AbstractMetadata> abstractMetadataCollection = accessTokenProvider.get();
        List<String> currentKeyList = new ArrayList<>();
        for (AbstractMetadata abstractMetadata : abstractMetadataCollection) {
            AccessInfoMeta currentValue = readOnlyCacheMap.get(abstractMetadata.getCacheKey());
            if (currentValue == null) {
                readOnlyCacheMap.put(abstractMetadata.getCacheKey(), (AccessInfoMeta) abstractMetadata);
                currentValue = (AccessInfoMeta) abstractMetadata;
            }
            readWriteCacheMap.put(abstractMetadata.getCacheKey(), (AccessInfoMeta) abstractMetadata);
            currentKeyList.add(abstractMetadata.getCacheKey());
        }
        // 移除无效KEY
        List<String> unValidKeyList = abstractMetadataCollection.stream().map(item -> {
            return item.getCacheKey();
        }).filter(item -> !currentKeyList.contains(item)).collect(Collectors.toList());
        unValidKeyList.stream().forEach(key -> {
            this.remove(key);
        });
    }
}
