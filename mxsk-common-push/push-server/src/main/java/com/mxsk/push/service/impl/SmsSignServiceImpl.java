package com.mxsk.push.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mxsk.push.cache.AccessInfoMeta;
import com.mxsk.push.cache.MetaCache;
import com.mxsk.push.converter.SmsSignConverter;
import com.mxsk.push.dto.Result;
import com.mxsk.push.dto.SmsSignDTO;
import com.mxsk.push.dto.SmsSignQueryRequestDTO;
import com.mxsk.push.dto.SmsSignQueryResponseDTO;
import com.mxsk.push.mapper.SmsSignMapper;
import com.mxsk.push.model.SmsSign;
import com.mxsk.push.service.SmsSignService;
import com.mxsk.push.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 短信签名service
 * @author: zhengguican
 * create in 2021/5/28 9:50
 */
@Service
public class SmsSignServiceImpl implements SmsSignService {

    @Autowired
    private SmsSignMapper smsSignMapper;

    @Autowired
    private MetaCache metaCache;

    @Override
    public Result<List<SmsSignQueryResponseDTO>> querySmsSign(SmsSignQueryRequestDTO smsSignQueryRequestDTO) {
        AccessInfoMeta accessInfoMeta = (AccessInfoMeta) metaCache.get(smsSignQueryRequestDTO.getTenantId().toString());
        SmsUtil.checkAccessInfoNotEmpty(accessInfoMeta, smsSignQueryRequestDTO.getTenantId());
        QueryWrapper<SmsSign> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SmsSign::getAccountId, accessInfoMeta.getAccountId());
        List<SmsSign> smsSignList = this.smsSignMapper.selectList(queryWrapper);
        List<SmsSignQueryResponseDTO> smsSignQueryRequestDTOList = smsSignList.stream().map(item -> {
            return SmsSignConverter.INSTANCE.domain2QuerySignDTO(item);
        }).collect(Collectors.toList());
        return Result.success(smsSignQueryRequestDTOList);
    }

    @Override
    public List<SmsSignDTO> querySmsSignByAuditStatus(String auditStatus) {
        return smsSignMapper.querySmsSignDTOByAuditStatus(auditStatus);
    }
}
