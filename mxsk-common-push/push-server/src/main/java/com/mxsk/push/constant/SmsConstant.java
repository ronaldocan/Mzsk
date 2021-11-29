package com.mxsk.push.constant;

/**
 * 短信常量类
 *
 * @author: zhengguican
 * create in 2021/5/20 14:17
 */
public interface SmsConstant {

    /**
     * 发送短信上限
     */
    Integer SMS_SEND_COUNT_LIMIT = Integer.valueOf(100);

    /**
     * 批量新增记录上限 DB
     */
    Integer DB_SAVE_SMS_BATCH_SIZE = Integer.valueOf(50);

    /**
     * 批量更新记录上限 DB
     */
    Integer DB_UPDATE_SMS_BATCH_SIZE = Integer.valueOf(40);
}
