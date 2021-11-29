package com.mxsk.push.service.impl;

import com.mxsk.push.dto.SmsSignDTO;
import com.mxsk.push.mapper.SmsSignMapper;
import com.mxsk.push.service.SmsSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhengguican
 * create in 2021/5/28 9:50
 */
@Service
public class SmsSignServiceImpl implements SmsSignService {

    @Autowired
    private SmsSignMapper smsSignMapper;

    @Override
    public List<SmsSignDTO> querySmsSignByAuditStatus(String auditStatus) {
        return smsSignMapper.querySmsSignDTOByAuditStatus(auditStatus);
    }
}
