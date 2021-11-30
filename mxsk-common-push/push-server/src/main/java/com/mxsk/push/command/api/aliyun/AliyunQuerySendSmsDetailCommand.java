package com.mxsk.push.command.api.aliyun;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mxsk.push.command.AbstractSmsCommand;
import com.mxsk.push.command.spi.aliyun.AliyunSmsClientAdapter;
import com.mxsk.push.dto.QuerySmsDetailResponseDTO;
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
 * 阿里云查询发送短信状态命令
 * @author: zhengguican
 * create in 2021/5/20 16:23
 */
@Slf4j
@Data
@Builder
public class AliyunQuerySendSmsDetailCommand extends AbstractSmsCommand {

    /**
     * 请求序列号
     */
    private String requestId;

    /**
     * 发送日期
     */
    private String sendDate;

    /**
     * 页容量
     */
    private Integer pageSize;

    /**
     * 当前页
     */
    private Integer currentPage;

    private String phoneNumber;

    @Override
    public Result execute() throws SmsCommandException {
        if (null == smsClient) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "获取短信平台账号配置异常");
        }
        CommonRequest request = SmsUtil.buildAliyunRequest(AliyunSmsCommandEnum.QUERY_SEND_DETAILS.getCode());
        request.putQueryParameter("BizId", requestId);
        request.putQueryParameter("CurrentPage", currentPage.toString());
        request.putQueryParameter("PageSize", "10");
        request.putQueryParameter("PhoneNumber", phoneNumber);
        request.putQueryParameter("SendDate", sendDate);
        AliyunSmsClientAdapter aliyunSmsClientAdapter = (AliyunSmsClientAdapter) smsClient;
        try {
            CommonResponse response = aliyunSmsClientAdapter.getCommonResponse(request);
            QuerySmsDetailResponseDTO responseDTO = SmsUtil.jsonToPojo(response.getData(),
                    QuerySmsDetailResponseDTO.class);
            return Result.success(responseDTO);
        } catch (ClientException e) {
            String errorMsg = String.format("阿里云短信平台请求查询发送短信状态异常，租户ID:%s,请求内容:%s", getAccessInfoMeta().getTenantId(), this.toString());
            log.error(errorMsg);
            throw new SmsCommandException(ErrorCode.QUERY_SMS_SEND_STATUS, errorMsg);
        }
    }

}
