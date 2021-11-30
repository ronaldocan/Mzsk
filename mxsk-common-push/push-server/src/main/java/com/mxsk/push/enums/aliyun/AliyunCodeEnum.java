package com.mxsk.push.enums.aliyun;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 阿里云响应码枚举
 * @author: zhengguican
 * create in 2021/5/21 16:54
 */
@AllArgsConstructor
@Getter
public enum AliyunCodeEnum {

    OK("OK", "OK"),
    ISV_SMS_SIGNATURE_SCENE_ILLEGAL("isv.SMS_SIGNATURE_SCENE_ILLEGAL", "短信所使用签名场景非法"),
    ISV_EXTEND_CODE_ERROR("isv.EXTEND_CODE_ERROR", "扩展码使用错误，相同的扩展码不可用于多个签名"),
    ISV_DOMESTIC_NUMBER_NOT_SUPPORTED("isv.DOMESTIC_NUMBER_NOT_SUPPORTED", "国际/港澳台消息模板不支持发送境内号码"),
    ISV_DENY_IP_RANGE("isv.DENY_IP_RANGE", "源IP地址所在的地区被禁用"),
    ISV_DAY_LIMIT_CONTROL("isv.DAY_LIMIT_CONTROL", "触发日发送限额"),
    ISV_SMS_CONTENT_ILLEGAL("isv.SMS_CONTENT_ILLEGAL", "短信内容包含禁止发送内容"),
    ISV_SMS_SIGN_ILLEGAL("isv.SMS_SIGN_ILLEGAL", "签名禁止使用"),
    ISP_RAM_PERMISSION_DENY("isp.RAM_PERMISSION_DENY", "RAM权限DENY"),
    ISV_OUT_OF_SERVICE("isv.OUT_OF_SERVICE", "业务停机"),
    ISV_PRODUCT_UN_SUBSCRIPT("isv.PRODUCT_UN_SUBSCRIPT", "未开通云通信产品的阿里云客户"),
    ISV_PRODUCT_UNSUBSCRIBE("isv.PRODUCT_UNSUBSCRIBE", "产品未开通"),
    ISV_ACCOUNT_NOT_EXISTS("isv.ACCOUNT_NOT_EXISTS", "账户不存在"),
    ISV_ACCOUNT_ABNORMAL("isv.ACCOUNT_ABNORMAL", "账户异常"),
    ISV_SMS_TEMPLATE_ILLEGAL("isv.SMS_TEMPLATE_ILLEGAL", "短信模版不合法"),
    ISV_SMS_SIGNATURE_ILLEGAL("isv.SMS_SIGNATURE_ILLEGAL", "短信签名不合法"),
    ISV_INVALID_PARAMETERS("isv.INVALID_PARAMETERS", "参数异常"),
    ISP_SYSTEM_ERROR("isp.SYSTEM_ERROR", "系统错误"),
    ISV_MOBILE_NUMBER_ILLEGAL("isv.MOBILE_NUMBER_ILLEGAL", "非法手机号"),
    ISV_MOBILE_COUNT_OVER_LIMIT("isv.MOBILE_COUNT_OVER_LIMIT", "手机号码数量超过限制"),
    ISV_TEMPLATE_MISSING_PARAMETERS("isv.TEMPLATE_MISSING_PARAMETERS", "模版缺少变量"),
    ISV_BUSINESS_LIMIT_CONTROL("isv.BUSINESS_LIMIT_CONTROL", "业务限流"),
    ISV_INVALID_JSON_PARAM("isv.INVALID_JSON_PARAM", "JSON参数不合法，只接受字符串值"),
    ISV_BLACK_KEY_CONTROL_LIMIT("isv.BLACK_KEY_CONTROL_LIMIT", "黑名单管控"),
    ISV_PARAM_LENGTH_LIMIT("isv.PARAM_LENGTH_LIMIT", "参数超出长度限制"),
    ISV_PARAM_NOT_SUPPORT_URL("isv.PARAM_NOT_SUPPORT_URL", "不支持URL"),
    ISV_AMOUNT_NOT_ENOUGH("isv.AMOUNT_NOT_ENOUGH", "账户余额不足"),
    ISV_TEMPLATE_PARAMS_ILLEGAL("isv.TEMPLATE_PARAMS_ILLEGAL", "模版变量里包含非法关键字"),
    ISV_SIGN_COUNT_OVER_LIMIT("isv.SIGN_COUNT_OVER_LIMIT", "一个自然日中申请签名数量超过限制。"),
    ISV_TEMPLATE_COUNT_OVER_LIMIT("isv.TEMPLATE_COUNT_OVER_LIMIT", "一个自然日中申请模板数量超过限制。"),
    ISV_SIGN_NAME_ILLEGAL("isv.SIGN_NAME_ILLEGAL", "签名名称不符合规范。"),
    ISV_SIGN_FILE_LIMIT("isv.SIGN_FILE_LIMIT", "签名认证材料附件大小超过限制。"),
    ISV_SIGN_OVER_LIMIT("isv.SIGN_OVER_LIMIT", "签名字符数量超过限制。"),
    ISV_TEMPLATE_OVER_LIMIT("isv.TEMPLATE_OVER_LIMIT", "签名字符数量超过限制。"),
    ISV_SHORTURL_OVER_LIMIT("isv.SHORTURL_OVER_LIMIT", "一天创建短链数量超过限制"),
    ISV_NO_AVAILABLE_SHORT_URL("isv.NO_AVAILABLE_SHORT_URL", "无有效短链"),
    ISV_SHORTURL_NAME_ILLEGAL("isv.SHORTURL_NAME_ILLEGAL", "短链名称非法"),
    ISV_SOURCEURL_OVER_LIMIT("isv.SOURCEURL_OVER_LIMIT", "原始链接字符数量超过限制"),
    ISV_SHORTURL_TIME_ILLEGAL("isv.SHORTURL_TIME_ILLEGAL", "短链有效期期限超过限制"),
    ISV_PHONENUMBERS_OVER_LIMIT("isv.PHONENUMBERS_OVER_LIMIT", "上传手机号个数超过上限"),
    ISV_SHORTURL_STILL_AVAILABLE("isv.SHORTURL_STILL_AVAILABLE", "原始链接生成的短链仍在有效期内"),
    ISV_SHORTURL_NOT_FOUND("isv.SHORTURL_NOT_FOUND", "没有可删除的短链"),
    ISV_ERROR_SIGN_NOT_MODIFY("isv.ERROR_SIGN_NOT_MODIFY", "签名不支持修改");

    private String code;

    private String desc;

    public static AliyunCodeEnum of(String code) {
        for (AliyunCodeEnum codeEnum : AliyunCodeEnum.values()) {
            if (codeEnum.getCode().equals(code)) {
                return codeEnum;
            }
        }
        return null;
    }
}
