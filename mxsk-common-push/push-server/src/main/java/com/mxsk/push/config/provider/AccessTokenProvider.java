package com.mxsk.push.config.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mxsk.push.cache.AbstractMetadata;
import com.mxsk.push.converter.SmsAccountConverter;
import com.mxsk.push.enums.SmsAccountStateEnum;
import com.mxsk.push.mapper.SmsAccountMapper;
import com.mxsk.push.model.SmsAccount;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 短信平台账号请求配置获取类
 * @author: zhengguican
 * create in 2021/5/12 14:23
 */
public class AccessTokenProvider implements MetadataProvider {

    private SmsAccountMapper smsAccountMapper;

    public AccessTokenProvider(SmsAccountMapper smsAccountMapper) {
        this.smsAccountMapper = smsAccountMapper;
    }

    @Override
    public Collection<AbstractMetadata> get() {
        QueryWrapper<SmsAccount> queryChannelWrapper = new QueryWrapper<>();
        queryChannelWrapper.lambda()
                .eq(SmsAccount::getStatus, SmsAccountStateEnum.ENABLE.getStatus());
        List<SmsAccount> accountList = smsAccountMapper.selectList(queryChannelWrapper);
        List<AbstractMetadata> metadataList = accountList.stream().map(item -> {
            return SmsAccountConverter.INSTANCE.domain2AccessInfoMeta(item);
        }).collect(Collectors.toList());
        return metadataList;
    }
}
