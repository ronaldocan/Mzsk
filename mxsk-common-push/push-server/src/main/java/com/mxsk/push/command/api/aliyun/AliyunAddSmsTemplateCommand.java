package com.mxsk.push.command.api.aliyun;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mxsk.push.command.AbstractSmsCommand;
import com.mxsk.push.command.spi.aliyun.AliyunSmsClientAdapter;
import com.mxsk.push.dto.AddSmsTemplateResponseDTO;
import com.mxsk.push.dto.Result;
import com.mxsk.push.enums.ResultCodeEnum;
import com.mxsk.push.enums.aliyun.AliyunSmsCommandEnum;
import com.mxsk.push.exception.ErrorCode;
import com.mxsk.push.exception.SmsCommandException;
import com.mxsk.push.util.SmsUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云添加短信模板命令
 * @author: zhengguican
 * create in 2021/5/20 14:09
 */
@Builder
@Slf4j
public class AliyunAddSmsTemplateCommand extends AbstractSmsCommand {

    /**
     * 短信模板类型
     */
    private String templateType;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 备注
     */
    private String remark;

    private static final String ACTION = "AddSmsTemplate";

    @Override
    public Result execute() throws SmsCommandException {
        if (null == smsClient) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "获取短信平台账号配置异常");
        }
        CommonRequest request = SmsUtil.buildAliyunRequest(AliyunSmsCommandEnum.ADD_SMS_TEMPLATE.getCode());
        request.putQueryParameter("TemplateType", templateType);
        request.putQueryParameter("TemplateName", templateName);
        request.putQueryParameter("TemplateContent", templateContent);
        request.putQueryParameter("Remark", remark);
        AliyunSmsClientAdapter aliyunSmsClientAdapter = (AliyunSmsClientAdapter) smsClient;
        try {
            CommonResponse response = aliyunSmsClientAdapter.getCommonResponse(request);
            AddSmsTemplateResponseDTO responseDTO = JSONUtil.toBean(response.getData(), AddSmsTemplateResponseDTO.class);
            return Result.success(responseDTO);
        } catch (ClientException e) {
            String errorMsg = String.format("阿里云短信平台请求创建模板异常，租户ID:%s,请求内容:%s", getAccessInfoMeta().getTenantId(), this.toString());
            log.error(errorMsg);
            throw new SmsCommandException(ErrorCode.CREATE_TEMPLATE_ERROR, errorMsg);
        }
    }

    @Override
    public String toString() {
        return "AliyunAddSmsTemplateCommand{" +
                "templateType='" + templateType + '\'' +
                ", templateName='" + templateName + '\'' +
                ", templateContent='" + templateContent + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
