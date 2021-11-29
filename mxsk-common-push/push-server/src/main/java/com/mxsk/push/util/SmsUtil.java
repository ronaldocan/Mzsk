package com.mxsk.push.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.http.MethodType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxsk.push.cache.AccessInfoMeta;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: zhengguican
 * create in 2021/5/17 17:57
 */
public class SmsUtil {

    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     *                 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对象转换成json字符串
     *
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建阿里云request
     *
     * @param action
     * @return
     */
    public static CommonRequest buildAliyunRequest(String action) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction(action);
        return request;
    }

    /**
     * 检查平台账号配置信息是否存在
     *
     * @param accessInfoMeta
     * @param tenantId
     */
    public static void checkAccessInfoNotEmpty(AccessInfoMeta accessInfoMeta, Long tenantId) {
        if (null == accessInfoMeta) {
            String errorMsg = String.format("获取短信平台账号配置信息失败，租户ID:%s", tenantId);
            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * 获取文件后缀名
     *
     * @param cFile
     * @return
     */
    public static String getFileExtension(MultipartFile cFile) {
        String originalFileName = cFile.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }
}
