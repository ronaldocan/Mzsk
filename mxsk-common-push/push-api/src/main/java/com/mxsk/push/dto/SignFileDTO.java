package com.mxsk.push.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 短信签名文件dto
 *
 * @author: zhengguican create in 2021/5/20 16:19
 */
@Data
@Builder
public class SignFileDTO {

  /** 签名的证明文件格式，支持上传多张图片。当前支持jpg、png、gif或jpeg格式的图片。个别场景下，申请签名需要上传证明文件 */
  private String fileSuffix;

  /** 签名的资质证明文件经base64编码后的字符串。图片不超过2 MB */
  private String fileContents;
}
