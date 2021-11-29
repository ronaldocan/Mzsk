package com.mxsk.push.cache;


import lombok.Data;

/**
 * @author: zhengguican
 * create in 2021/5/12 14:17
 */
@Data
public abstract class AbstractMetadata {

    private String accessKey;

    private String secret;

    private Long tenantId;

    private Integer accountId;

    private String endpoint;

    private String platform;

    public AbstractMetadata(String accessKey, String secret, Long tenantId, Integer accountId, String endpoint, String platform) {
        this.accessKey = accessKey;
        this.secret = secret;
        this.tenantId = tenantId;
        this.accountId = accountId;
        this.endpoint = endpoint;
        this.platform = platform;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public AbstractMetadata() {
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * 获取缓存KEY
     *
     * @return
     */
    public abstract String getCacheKey();
}
