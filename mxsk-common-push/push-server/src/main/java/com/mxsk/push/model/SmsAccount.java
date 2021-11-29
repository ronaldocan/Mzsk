package com.mxsk.push.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 短信平台帐号记录表实体类
 *
 * @author zhengguican
 * @since 2021-05-24 13:35:49
 */
@Data
@Builder
public class SmsAccount {

    /**
     * 账号记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 账号访问key
     */
    private String accessKey;

    /**
     * 账号访问密钥
     */
    private String secret;

    /**
     * 状态
     */
    private String status;

    /**
     * 服务地址
     */
    private String endPoint;

    /**
     * 平台
     */
    private String platform;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}