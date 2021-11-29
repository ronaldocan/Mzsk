package com.mxsk.push.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 短信签名表实体类
 *
 * @author zhengguican
 * @since 2021-05-24 16:35:13
 */
@Data
@Builder
public class SmsSign {

    /**
     * 签名记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 短信平台账号记录ID
     */
    private Integer accountId;

    /**
     * 签名名称
     */
    private String signName;

    /**
     * 签名来源。其中：0：企事业单位的全称或简称。1：工信部备案网站的全称或简称。2：APP应用的全称或简称。3：公众号或小程序的全称或简称。4：电商平台店铺名的全称或简称。5：商标名的全称或简称
     */
    private String signSource;

    /**
     * 备注
     */
    private String remark;

    /**
     * 0:待审核、1：审核通过、2：审核不通过
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}