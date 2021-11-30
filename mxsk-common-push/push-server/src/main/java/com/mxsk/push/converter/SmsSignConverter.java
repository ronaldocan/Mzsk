package com.mxsk.push.converter;

import com.mxsk.push.dto.SmsSignQueryResponseDTO;
import com.mxsk.push.model.SmsSign;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 短信签名实体转换器
 *
 * @author: zhengguican
 * create in 2021/5/24 16:19
 */
public interface SmsSignConverter {

    SmsSignConverter INSTANCE = Mappers.getMapper(SmsSignConverter.class);

    /**
     * DB实体转换为DTO
     *
     * @param smsSign 短信签名记录
     * @return
     */
    @Mappings({
            @Mapping(source = "signName", target = "signName"),
            @Mapping(source = "signSource", target = "signSource"),
            @Mapping(source = "remark", target = "remark"),
            @Mapping(source = "createDate", target = "createDate")
    })
    SmsSignQueryResponseDTO domain2QuerySignDTO(SmsSign smsSign);
}
