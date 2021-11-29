CREATE TABLE `sms_record`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `account_id`    int          NOT NULL COMMENT '帐号ID',
    `template_code` varchar(32)  NOT NULL COMMENT '模版编号',
    `sign_name`     varchar(32)  NOT NULL COMMENT '短信签名',
    `phone`         varchar(32)  NOT NULL COMMENT '手机号',
    `biz_id`        varchar(32)  NOT NULL COMMENT '请求序列号',
    `status`        varchar(8)   NOT NULL COMMENT '短信发送状态',
    `content`       varchar(256) NOT NULL COMMENT '短信内容',
    `update_time`   datetime(3) NOT NULL COMMENT '更新时间',
    `create_time`   datetime(3) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `sms_account`
(
    `id`          int         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`   int         NOT NULL COMMENT '租户ID',
    `access_key`  varchar(32) NOT NULL COMMENT '平台访问key',
    `secret`      varchar(32) NOT NULL COMMENT '平台访问密钥',
    `status`      varchar(8)  NOT NULL COMMENT '平台可用状态',
    `end_point`   varchar(32) NOT NULL COMMENT '平台访问终端',
    `platform`    varchar(8)  NOT NULL COMMENT '平台类型',
    `update_time` datetime(3) NOT NULL COMMENT '更新时间',
    `create_time` datetime(3) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `sms_sign`
(
    `id`          int          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `account_id`  int          NOT NULL COMMENT '短信平台帐号记录ID',
    `sign_name`   varchar(32)  NOT NULL COMMENT '短信签名',
    `sign_source` varchar(8)   NOT NULL COMMENT '短信签名来源',
    `remark`      varchar(128) NOT NULL COMMENT '签名备注',
    `status`      varchar(8)   NOT NULL COMMENT '短信签名审核状态',
    `update_time` datetime(3) NOT NULL COMMENT '更新时间',
    `create_time` datetime(3) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


CREATE TABLE `sms_template`
(
    `id`          int         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `account_id`  int         NOT NULL COMMENT '短信平台帐号记录ID',
    `code`        varchar(32) NOT NULL COMMENT '短信模版编号',
    `content`     varchar(8)  NOT NULL COMMENT '短信模版内容',
    `name`        varchar(64) NOT NULL COMMENT '短信模版名称',
    `type`        varchar(16) NOT NULL COMMENT '短信模版类型',
    `status`      varchar(8)  NOT NULL COMMENT '短信模版审核状态',
    `update_time` datetime(3) NOT NULL COMMENT '更新时间',
    `create_time` datetime(3) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
