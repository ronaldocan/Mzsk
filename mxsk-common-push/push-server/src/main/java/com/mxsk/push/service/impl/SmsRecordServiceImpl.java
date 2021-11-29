package com.mxsk.push.service.impl;

import com.mxsk.push.mapper.SmsRecordMapper;
import com.mxsk.push.model.SmsRecord;
import com.mxsk.push.service.SmsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhengguican
 * create in 2021/5/27 10:54
 */
@Service
public class SmsRecordServiceImpl implements SmsRecordService {

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Override
    public void updateSmsSendStatus(List<SmsRecord> smsRecordList) {
        this.smsRecordMapper.updateSmsSendStatusBatch(smsRecordList);
    }
}
