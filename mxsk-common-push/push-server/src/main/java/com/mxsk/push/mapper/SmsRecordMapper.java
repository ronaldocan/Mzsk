package com.mxsk.push.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mxsk.push.model.SmsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (SmsRecord)表 Mapper
 *
 * @author zhengguican
 * @since 2021-05-24 16:34:59
 */
@Mapper
@Repository
public interface SmsRecordMapper extends BaseMapper<SmsRecord> {

    /**
     * 批量插入
     *
     * @param list
     */
    void insertBatch(List<SmsRecord> list);

    /**
     * 批量更新短信记录内容及状态
     *
     * @param smsRecordList
     */
    void updateSmsSendStatusBatch(List<SmsRecord> smsRecordList);
}