package com.mxsk.push.command.api.aliyun;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.mxsk.push.command.AbstractSmsCommand;
import com.mxsk.push.command.spi.aliyun.AliyunSmsClientAdapter;
import com.mxsk.push.dto.Result;
import com.mxsk.push.dto.SmsResponseDTO;
import com.mxsk.push.enums.AliyunSmsCommandEnum;
import com.mxsk.push.enums.ResultCodeEnum;
import com.mxsk.push.exception.ErrorCode;
import com.mxsk.push.exception.SmsCommandException;
import com.mxsk.push.util.SmsUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 批量发送短信命令
 * @author: zhengguican
 * create in 2021/5/20 15:05
 */
@Builder
@Data
@Slf4j
public class AliyunSendBatchSmsCommand extends AbstractSmsCommand {

    private String templateCode;
    private List<String> phoneList;
    private List<String> signNameList;
    private List<Map<String, String>> paramList;

    @Override
    public Result execute() throws SmsCommandException {

        if (null == smsClient) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "获取短信平台账号配置异常");
        }
        CommonRequest request = SmsUtil.buildAliyunRequest(AliyunSmsCommandEnum.SEND_BATCH_SMS.getCode());
        request.putQueryParameter("PhoneNumberJson", SmsUtil.objectToJson(phoneList));
        request.putQueryParameter("SignNameJson", SmsUtil.objectToJson(signNameList));
        request.putQueryParameter("TemplateCode", templateCode);

        if (!CollectionUtils.isEmpty(paramList)) {
            request.putQueryParameter("TemplateParamJson", SmsUtil.objectToJson(paramList));
        }
        AliyunSmsClientAdapter aliyunSmsClientAdapter = (AliyunSmsClientAdapter) smsClient;
        try {
            CommonResponse response = aliyunSmsClientAdapter.getCommonResponse(request);
            SmsResponseDTO smsResponseDTO = JSONUtil.toBean(response.getData(), SmsResponseDTO.class);
            return Result.success(smsResponseDTO);
        } catch (ClientException e) {
            String errorMsg = String.format("阿里云短信平台批量发送短信异常，租户ID:%s,请求内容:%s", getAccessInfoMeta().getTenantId(), this.toString());
            log.error(errorMsg);
            throw new SmsCommandException(ErrorCode.SMS_SEND_ERROR, errorMsg);
        }
    }

    public static void main(String[] args) {

        DefaultProfile profile = DefaultProfile.getProfile("ap-northeast-1", "LTAI4G6XamAwfrevpp2fV4eB", "tb2sW0yXdH8yUHvz6T1uuwJkSZFcFS");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", "13826116514");
        request.putQueryParameter("SignName", "知藍プラットフォーム");
        request.putQueryParameter("TemplateCode", "SMS_209171411");
        request.putQueryParameter("TemplateParam", "{\"userName\":\"XX\",\"date\":\"XX\",\"url\":\"URL\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
