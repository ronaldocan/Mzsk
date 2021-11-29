package com.mxsk.push.converter;

import com.mxsk.push.dto.SmsTemplateQueryResponseDTO;
import com.mxsk.push.model.SmsTemplate;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 短信模版实体转换器
 * @author: zhengguican
 * create in 2021/5/24 16:19
 */
public interface SmsTemplateConverter {

    SmsTemplateConverter INSTANCE = Mappers.getMapper(SmsTemplateConverter.class);

    @Mappings({
            @Mapping(source = "templateType", target = "templateType"),
            @Mapping(source = "templateContent", target = "templateContent"),
            @Mapping(source = "templateStatus", target = "templateStatus"),
            @Mapping(source = "createDate", target = "createDate")
    })
    SmsTemplateQueryResponseDTO domain2QueryTemplateDTO(SmsTemplate smsTemplate);
}
