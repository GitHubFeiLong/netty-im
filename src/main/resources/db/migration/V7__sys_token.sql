DROP TABLE IF EXISTS `sys_auth_token_blacklist`;
DROP TABLE IF EXISTS `sys_token`;
CREATE TABLE `sys_token`
(
    `id`          varchar(32) NOT NULL COMMENT 'TOKEN ID',
    `token`       text        NOT NULL COMMENT 'TOKEN 内容',
    `type`        tinyint(4)  NOT NULL COMMENT '类型（1-IM用户；2-SYS用户）',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户ID',
    `status`      tinyint(4)  NOT NULL COMMENT '状态（0-可用；1-不可用）',
    `remark`      varchar(255)         DEFAULT NULL COMMENT '备注',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='认证用户TOKEN';
