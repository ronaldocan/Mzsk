package com.mxsk.push.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mxsk.push.cache.AccessInfoMeta;
import com.mxsk.push.cache.MetaCache;
import com.mxsk.push.converter.SmsTemplateConverter;
import com.mxsk.push.dto.Result;
import com.mxsk.push.dto.SmsTemplateDTO;
import com.mxsk.push.dto.SmsTemplateQueryRequestDTO;
import com.mxsk.push.dto.SmsTemplateQueryResponseDTO;
import com.mxsk.push.mapper.SmsTemplateMapper;
import com.mxsk.push.model.SmsTemplate;
import com.mxsk.push.service.SmsTemplateService;
import com.mxsk.push.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhengguican
 * create in 2021/5/26 17:02
 */
@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {

    @Autowired
    private MetaCache metaCache;

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    @Override
    public Result<List<SmsTemplateQueryResponseDTO>> querySmsTemplate(SmsTemplateQueryRequestDTO smsTemplateQueryRequestDTO) {
        AccessInfoMeta accessInfoMeta = (AccessInfoMeta) metaCache.get(smsTemplateQueryRequestDTO.getTenantId().toString());
        SmsUtil.checkAccessInfoNotEmpty(accessInfoMeta, smsTemplateQueryRequestDTO.getTenantId());
        QueryWrapper<SmsTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SmsTemplate::getAccountId, accessInfoMeta.getAccountId());
        List<SmsTemplate> smsTemplateList = this.smsTemplateMapper.selectList(queryWrapper);
        List<SmsTemplateQueryResponseDTO> smsTemplateQueryResponseDTOList = smsTemplateList.stream().map(item -> {
            return SmsTemplateConverter.INSTANCE.domain2QueryTemplateDTO(item);
        }).collect(Collectors.toList());
        return Result.success(smsTemplateQueryResponseDTOList);
    }

    @Override
    public List<SmsTemplateDTO> queryTemplateByAuditStatus(String auditStatus) {
        return this.smsTemplateMapper.queryTemplateDTOByAuditStatus(auditStatus);
    }
}
