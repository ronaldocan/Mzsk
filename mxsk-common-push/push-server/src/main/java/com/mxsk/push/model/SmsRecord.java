package com.mxsk.push.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 短信记录表实体类
 *
 * @author zhengguican
 * @since 2021-05-24 16:35:02
 */
@Data
@Builder
public class SmsRecord {

    /**
     * 短信记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 短信平台账号记录ID
     */
    private Integer accountId;

    /**
     * 短信模板ID
     */
    private String templateCode;

    /**
     * 签名名称
     */
    private String signName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送状态
     */
    private String status;

    /**
     * 平台请求序列号
     */
    private String bizId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Date updateTime;
}