package com.mxsk.push.api;

import com.mxsk.push.dto.*;
import com.mxsk.push.enums.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/** @author: zhengguican create in 2021/5/20 10:13 */
@Api(tags = "短信平台操作")
public interface SmsPlatformApi {

  /**
   * 批量发送短信
   *
   * @param sendSmsRequestDTO
   * @return
   */
  @RequestMapping(value = "/sms/send", method = RequestMethod.POST)
  @ApiOperation(value = "发送短信", notes = "发送短信", response = Void.class)
  default Result<Void> sendSms(@RequestBody SendSmsRequestDTO sendSmsRequestDTO) {
    return Result.error(ResultCodeEnum.FAIL.getCode(), "");
  }

  /**
   * 批量发送短信
   *
   * @param sendBatchSmsRequestDTO
   * @return
   */
  @RequestMapping(value = "/sms/send_batch", method = RequestMethod.POST)
  @ApiOperation(value = "批量发送短信", notes = "批量发送短信", response = Void.class)
  default Result<Void> sendBatchSms(@RequestBody SendBatchSmsRequestDTO sendBatchSmsRequestDTO) {
    return Result.error(ResultCodeEnum.FAIL.getCode(), "");
  }

  /**
   * 添加短信模板
   *
   * @param addSmsTemplateRequestDTO
   * @return
   */
  @RequestMapping(value = "/sms/template/add", method = RequestMethod.POST)
  @ApiOperation(value = "添加短信模板", response = String.class)
  default Result<String> addSmsTemplate(
      @RequestBody AddSmsTemplateRequestDTO addSmsTemplateRequestDTO) {
    return Result.error(ResultCodeEnum.FAIL.getCode(), "");
  }

  /**
   * 短信模板查询
   *
   * @param smsTemplateQueryRequestDTO
   * @return
   */
  @RequestMapping(value = "/sms/template/query", method = RequestMethod.GET)
  @ApiOperation(value = "短信模板查询", response = SmsTemplateQueryResponseDTO.class)
  default Result<List<SmsTemplateQueryResponseDTO>> querySmsTemplate(
      @RequestBody SmsTemplateQueryRequestDTO smsTemplateQueryRequestDTO) {
    return Result.error(ResultCodeEnum.FAIL.getCode(), "");
  }

  /**
   * 短信签名查询
   *
   * @param smsSignQueryRequestDTO
   * @return
   */
  @RequestMapping(value = "/sms/sign/query", method = RequestMethod.GET)
  @ApiOperation(value = "短信签名查询")
  default Result<List<SmsSignQueryResponseDTO>> querySmsSign(
      @RequestBody SmsSignQueryRequestDTO smsSignQueryRequestDTO) {
    return Result.error(ResultCodeEnum.FAIL.getCode(), "");
  }

  /**
   * 添加短信签名
   *
   * @param smsRequestDTO
   * @return
   */
  @RequestMapping(value = "/sms/sign/add", method = RequestMethod.POST)
  @ApiOperation(value = "添加短信签名", notes = "添加短信签名", response = Void.class)
  default Result<Void> addSmsSign(@RequestBody AddSmsSignRequestDTO smsRequestDTO) {
    return Result.error(ResultCodeEnum.FAIL.getCode(), "");
  }

  /**
   * 添加短信签名
   *
   * @param tenantId
   * @param signName
   * @param signSource
   * @param remark
   * @param signFileList
   * @return
   */
  @RequestMapping(value = "/sign/add_with_file", method = RequestMethod.POST)
  @ApiOperation(value = "添加短信签名", notes = "添加短信签名", response = Void.class)
  default Result<Void> addSmsSignWithFile(
      @RequestParam("tenantId") Long tenantId,
      @RequestParam("signName") String signName,
      @RequestParam("signSource") String signSource,
      @RequestParam("remark") String remark,
      @RequestParam("signFileList") List<MultipartFile> signFileList) {
    return Result.error(ResultCodeEnum.FAIL.getCode(), "");
  }
}
