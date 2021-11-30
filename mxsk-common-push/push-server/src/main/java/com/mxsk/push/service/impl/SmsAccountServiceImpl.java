package com.mxsk.push.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mxsk.push.constant.SmsPlatformConstant;
import com.mxsk.push.dto.AddSmsAccountRequestDTO;
import com.mxsk.push.dto.Result;
import com.mxsk.push.enums.SmsAccountStateEnum;
import com.mxsk.push.mapper.SmsAccountMapper;
import com.mxsk.push.model.SmsAccount;
import com.mxsk.push.service.SmsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 短信平台帐号记录服务
 *
 * @author: zhengguican
 * create in 2021/7/16 15:02
 */
@Service
public class SmsAccountServiceImpl implements SmsAccountService {

    @Autowired
    private SmsAccountMapper smsAccountMapper;

    @Override
    public Result<Void> save(AddSmsAccountRequestDTO addSmsAccountRequestDTO) {

        // TODO zgc 待需求演进，添加短信平台配置接口
        LambdaQueryWrapper<SmsAccount> countMapper = new QueryWrapper<SmsAccount>().lambda()
                .eq(SmsAccount::getTenantId, addSmsAccountRequestDTO.getTenantId());
        Integer count = this.smsAccountMapper.selectCount(countMapper);
        if (count > 0) {
            return Result.success();
        }

        Date nowTime = new Date();
        SmsAccount smsAccount = SmsAccount.builder().accessKey(SmsPlatformConstant.ACCESS_KEY)
                .secret(SmsPlatformConstant.SECRET).endPoint("aliyun_default").status(SmsAccountStateEnum.ENABLE.getStatus())
                .tenantId(addSmsAccountRequestDTO.getTenantId()).createTime(nowTime).updateTime(nowTime)
                .platform("aliyun")
                .build();

        this.smsAccountMapper.insert(smsAccount);
        return Result.success();
    }

    /**
     * 检验新增记录参数是否有效
     *
     * @param addSmsAccountRequestDTO
     */
    private void checkCreateParamValid(AddSmsAccountRequestDTO addSmsAccountRequestDTO) {
        Objects.requireNonNull(addSmsAccountRequestDTO.getAccessKey(), "平台账号访问key为空");
        Objects.requireNonNull(addSmsAccountRequestDTO.getPlatform(), "账号平台类型为空");
        Objects.requireNonNull(addSmsAccountRequestDTO.getSecret(), "平台账号访问密钥为空");
        Objects.requireNonNull(addSmsAccountRequestDTO.getTenantId(), "租户ID为空");
    }
}
