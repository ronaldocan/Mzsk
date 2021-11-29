package com.mxsk.push.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 短信记录实体转换器
 * @author: zhengguican
 * create in 2021/5/20 13:48
 */
@Mapper
public interface SmsRecordConverter {

    SmsRecordConverter INSTANCE = Mappers.getMapper(SmsRecordConverter.class);

//    @Mappings({
//            @Mapping(source = "signName", target = "signName"),
//            @Mapping(source = "signSource", target = "signSource"),
//            @Mapping(source = "remark", target = "remark"),
//            @Mapping(source = "createDate", target = "createDate")
//    })
//    SmsSignQueryResponseDTO domain2QuerySignDTO(SmsSign smsSign);
}
