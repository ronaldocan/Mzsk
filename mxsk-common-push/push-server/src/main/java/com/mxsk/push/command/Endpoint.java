package com.mxsk.push.command;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhengguican
 * create in 2021/5/20 10:58
 */
public class Endpoint {

    private static Map<String, Endpoint> CACHE = new HashMap<>();

    private String endpointId;

    private String url;

    public Endpoint(String endpointId, String url) {
        this.endpointId = endpointId;
        this.url = url;
        CACHE.put(endpointId, this);
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public static Endpoint of(String regionId) {
        return CACHE.get(regionId);
    }
}
