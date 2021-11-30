package com.mxsk.push.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mxsk.push.model.SmsAccount;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * (SmsAccount)表 Mapper
 * @author zhengguican
 * @since 2021-05-24 13:35:47
 */
@Mapper
@Repository
public interface SmsAccountMapper extends BaseMapper<SmsAccount> {

}