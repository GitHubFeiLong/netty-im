DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`              bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '系统用户ID',
    `username`        varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '用户名',
    `password`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '密码',
    `roles`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '用户角色列表，逗号分隔（如：ADMIN,USER）',
    `avatar`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '用户头像',
    `nickname`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '昵称',
    `mobile`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '联系方式',
    `email`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '用户邮箱',
    `gender`          tinyint(1)                                                            DEFAULT '1' COMMENT '性别(1-男 2-女 0-保密)',
    `valid_time`      datetime                                                     NOT NULL COMMENT '过期时间，不填永久有效',
    `enabled`         bit(1)                                                       NOT NULL DEFAULT b'1' COMMENT '激活状态：true 激活；false 未激活',
    `locked`          bit(1)                                                       NOT NULL DEFAULT b'0' COMMENT '锁定状态：true 锁定；false 未锁定',
    `last_login_time` datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '最近登录时间',
    `remark`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs         DEFAULT NULL COMMENT '备注',
    `version`         int(11)                                                      NOT NULL DEFAULT '1' COMMENT '版本号',
    `deleted`         bit(1)                                                       NOT NULL DEFAULT b'0' COMMENT '删除状态',
    `create_time`     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_username` (`username`) USING BTREE COMMENT '用户名唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC COMMENT ='系统用户';
