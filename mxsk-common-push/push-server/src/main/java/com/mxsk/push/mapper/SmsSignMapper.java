package com.mxsk.push.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mxsk.push.dto.SmsSignDTO;
import com.mxsk.push.model.SmsSign;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (SmsSign)表 Mapper
 *
 * @author zhengguican
 * @since 2021-05-24 16:35:11
 */
@Mapper
@Repository
public interface SmsSignMapper extends BaseMapper<SmsSign> {

    /**
     * 根据审核状态查找DTO
     *
     * @param auditStatus
     * @return
     */
    List<SmsSignDTO> querySmsSignDTOByAuditStatus(String auditStatus);
}