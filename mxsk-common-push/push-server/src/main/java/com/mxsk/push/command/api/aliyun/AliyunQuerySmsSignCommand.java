package com.mxsk.push.command.api.aliyun;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mxsk.push.command.AbstractSmsCommand;
import com.mxsk.push.command.spi.aliyun.AliyunSmsClientAdapter;
import com.mxsk.push.dto.QuerySignResponseDTO;
import com.mxsk.push.dto.Result;
import com.mxsk.push.enums.ResultCodeEnum;
import com.mxsk.push.enums.aliyun.AliyunSmsCommandEnum;
import com.mxsk.push.exception.ErrorCode;
import com.mxsk.push.exception.SmsCommandException;
import com.mxsk.push.util.SmsUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云查询短信签名命令
 * @author: zhengguican
 * create in 2021/5/20 16:23
 */
@Slf4j
@Data
@Builder
public class AliyunQuerySmsSignCommand extends AbstractSmsCommand {

    private String signName;

    @Override
    public Result execute() throws SmsCommandException {
        if (null == smsClient) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "获取短信平台账号配置异常");
        }
        CommonRequest request = SmsUtil.buildAliyunRequest(AliyunSmsCommandEnum.QUERY_SMS_SIGN.getCode());
        request.putQueryParameter("SignName", signName);
        AliyunSmsClientAdapter aliyunSmsClientAdapter = (AliyunSmsClientAdapter) smsClient;
        try {
            CommonResponse response = aliyunSmsClientAdapter.getCommonResponse(request);
            QuerySignResponseDTO smsResponseDTO = JSONUtil.toBean(response.getData(), QuerySignResponseDTO.class);
            return Result.success(smsResponseDTO);
        } catch (ClientException e) {
            String errorMsg = String.format("阿里云短信平台请求查询短信签名记录异常，租户ID:%s,请求内容:%s", getAccessInfoMeta().getTenantId(), this.toString());
            log.error(errorMsg);
            throw new SmsCommandException(ErrorCode.QUERY_SIGN_ERROR, errorMsg);
        }
    }
}
