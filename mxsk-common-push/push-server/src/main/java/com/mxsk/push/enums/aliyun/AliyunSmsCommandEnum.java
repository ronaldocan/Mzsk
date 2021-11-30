package com.mxsk.push.enums.aliyun;

/**
 * 阿里云短信接口枚举
 *
 * @author: zhengguican
 * create in 2021/5/20 15:05
 */
public enum AliyunSmsCommandEnum {

    ADD_SMS_TEMPLATE("AddSmsTemplate", "添加短信模板"),
    SEND_SMS("SendSms", "发送短信"),
    SEND_BATCH_SMS("SendBatchSms", "批量发送短信"),
    MODIFY_SMS_TEMPLATE("ModifySmsTemplate", "修改短信模板"),
    ADD_SMS_SIGN("AddSmsSign", "添加短信签名"),
    QUERY_SMS_SIGN("QuerySmsSign", "查询短信签名"),
    QUERY_SEND_DETAILS("QuerySendDetails", "查询发送短信状态"),
    QUERY_SMS_TEMPLATE("QuerySmsTemplate", "查询短信模板");

    private String code;

    private String desc;

    AliyunSmsCommandEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
