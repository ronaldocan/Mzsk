package com.mxsk.push.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mxsk.push.dto.SmsTemplateDTO;
import com.mxsk.push.model.SmsTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (SmsTemplate)表 Mapper
 * @author zhengguican
 * @since 2021-05-24 16:35:19
 */
@Mapper
@Repository
public interface SmsTemplateMapper extends BaseMapper<SmsTemplate> {

    /**
     * 根据审核状态查找记录
     *
     * @param auditStatus 审核状态
     * @return
     */
    List<SmsTemplateDTO> queryTemplateDTOByAuditStatus(String auditStatus);
}