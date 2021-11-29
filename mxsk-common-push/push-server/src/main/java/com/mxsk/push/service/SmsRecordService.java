package com.mxsk.push.service;

import com.mxsk.push.model.SmsRecord;

import java.util.List;

/**
 * 短信记录接口
 *
 * @author: zhengguican
 * create in 2021/5/27 10:54
 */
public interface SmsRecordService {

    /**
     * 批量更新短信内容
     *
     * @param smsRecordList
     */
    void updateSmsSendStatus(List<SmsRecord> smsRecordList);
}
