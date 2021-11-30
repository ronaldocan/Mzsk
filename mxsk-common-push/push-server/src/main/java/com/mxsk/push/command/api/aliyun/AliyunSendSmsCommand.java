package com.mxsk.push.command.api.aliyun;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mxsk.push.command.AbstractSmsCommand;
import com.mxsk.push.command.spi.aliyun.AliyunSmsClientAdapter;
import com.mxsk.push.dto.Result;
import com.mxsk.push.dto.SmsResponseDTO;
import com.mxsk.push.enums.ResultCodeEnum;
import com.mxsk.push.enums.aliyun.AliyunSmsCommandEnum;
import com.mxsk.push.exception.ErrorCode;
import com.mxsk.push.exception.SmsCommandException;
import com.mxsk.push.util.SmsUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 批量发送短信命令
 * @author: zhengguican
 * create in 2021/5/20 15:05
 */
@Builder
@Data
@Slf4j
public class AliyunSendSmsCommand extends AbstractSmsCommand {

    private String templateCode;
    private String phoneNumber;
    private String signName;
    private Map<String, String> paramMap;

    @Override
    public Result execute() throws SmsCommandException {
        if (null == smsClient) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "获取短信平台账号配置异常");
        }
        CommonRequest request = SmsUtil.buildAliyunRequest(AliyunSmsCommandEnum.SEND_SMS.getCode());
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);

        if (!CollectionUtils.isEmpty(paramMap)) {
            request.putQueryParameter("TemplateParam", SmsUtil.objectToJson(paramMap));
        }
        AliyunSmsClientAdapter aliyunSmsClientAdapter = (AliyunSmsClientAdapter) smsClient;
        try {
            CommonResponse response = aliyunSmsClientAdapter.getCommonResponse(request);
            SmsResponseDTO smsResponseDTO = JSONUtil.toBean(response.getData(), SmsResponseDTO.class);
            return Result.success(smsResponseDTO);
        } catch (ClientException e) {
            String errorMsg = String.format("阿里云短信平台发送短信异常，租户ID:%s,请求内容:%s", getAccessInfoMeta().getTenantId(), this.toString());
            log.error(errorMsg);
            throw new SmsCommandException(ErrorCode.SMS_SEND_ERROR, errorMsg);
        }
    }
}
