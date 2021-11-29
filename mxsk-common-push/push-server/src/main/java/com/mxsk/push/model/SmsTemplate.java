package com.mxsk.push.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 短信模版表实体类
 *
 * @author zhengguican
 * @since 2021-05-24 16:35:21
 */
@Data
@Builder
public class SmsTemplate {

    /**
     * 短信模板记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 账号ID
     */
    private Integer accountId;

    /**
     * 模板code
     */
    private String code;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板状态
     */
    private String status;

    /**
     * 模板类型
     */
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}