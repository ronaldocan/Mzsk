package com.mxsk.push.converter;

import com.mxsk.push.cache.AccessInfoMeta;
import com.mxsk.push.model.SmsAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 短信帐号实体转换器
 * @author: zhengguican
 * create in 2021/5/21 14:08
 */
@Mapper
public interface SmsAccountConverter {

    SmsAccountConverter INSTANCE = Mappers.getMapper(SmsAccountConverter.class);

    @Mappings({
            @Mapping(source = "id", target = "accountId"),
            @Mapping(source = "tenantId", target = "tenantId"),
            @Mapping(source = "accessKey", target = "accessKey"),
            @Mapping(source = "secret", target = "secret"),
            @Mapping(source = "platform", target = "platform"),
            @Mapping(source = "endPoint", target = "endpoint")
    })
    AccessInfoMeta domain2AccessInfoMeta(SmsAccount smsAccount);
}
