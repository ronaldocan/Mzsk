package com.mxsk.push.cache;


import lombok.Data;

/**
 * @author zhengguican
 * create-at 2021/2/7
 */
@Data
public class AccessInfoMeta extends AbstractMetadata {

    private String regionId;

    public AccessInfoMeta(String regionId) {
        this.regionId = regionId;
    }

    @Override
    public String getCacheKey() {

        return getTenantId().toString();
    }
}
