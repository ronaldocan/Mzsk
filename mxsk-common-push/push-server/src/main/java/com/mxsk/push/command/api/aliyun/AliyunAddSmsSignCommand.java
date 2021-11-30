package com.mxsk.push.command.api.aliyun;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mxsk.push.command.AbstractSmsCommand;
import com.mxsk.push.command.spi.aliyun.AliyunSmsClientAdapter;
import com.mxsk.push.dto.QuerySignResponseDTO;
import com.mxsk.push.dto.Result;
import com.mxsk.push.dto.SignFileDTO;
import com.mxsk.push.enums.ResultCodeEnum;
import com.mxsk.push.enums.aliyun.AliyunSmsCommandEnum;
import com.mxsk.push.exception.ErrorCode;
import com.mxsk.push.exception.SmsCommandException;
import com.mxsk.push.util.SmsUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 阿里云添加短信签名api
 * @author: zhengguican
 * create in 2021/5/20 15:18
 */
@Slf4j
@Data
@Builder
public class AliyunAddSmsSignCommand extends AbstractSmsCommand {

    private String signName;
    private String signSource;
    private String remark;
    private List<SignFileDTO> signFileList;

    @Override
    public Result execute() throws SmsCommandException {
        if (null == smsClient) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "获取短信平台账号配置异常");
        }
        //初始化acsClient,<accessKeyId>和"<accessSecret>"在短信控制台查询即可。
        CommonRequest request = SmsUtil.buildAliyunRequest(AliyunSmsCommandEnum.ADD_SMS_SIGN.getCode());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("SignSource", signSource);
        request.putQueryParameter("Remark", remark);
        if (!CollectionUtils.isEmpty(signFileList)) {
            for (int i = 1; i <= signFileList.size(); i++) {
                request.putQueryParameter("SignFileList." + i + ".FileSuffix", signFileList.get(i - 1).getFileSuffix());
                request.putQueryParameter("SignFileList." + i + ".FileContents", signFileList.get(i - 1).getFileContents());
            }
        }
        AliyunSmsClientAdapter aliyunSmsClientAdapter = (AliyunSmsClientAdapter) smsClient;
        try {
            CommonResponse response = aliyunSmsClientAdapter.getCommonResponse(request);
            QuerySignResponseDTO responseDTO = JSONUtil.toBean(response.getData(), QuerySignResponseDTO.class);
            return Result.success(responseDTO);
        } catch (ClientException e) {
            String errorMsg = String.format("阿里云短信平台请求创建签名异常，租户ID:%s,请求内容:%s", getAccessInfoMeta().getTenantId(), this.toString());
            log.error(errorMsg);
            throw new SmsCommandException(ErrorCode.CREATE_SIGN_ERROR, errorMsg);
        }
    }
}
