DROP TABLE IF EXISTS `sys_auth_token_blacklist`;
CREATE TABLE `sys_auth_token_blacklist`
(
    `id`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '认证token ID',
    `deleted`     bigint(20)                                                   NOT NULL DEFAULT '0' COMMENT '删除状态（0-未删除；>0删除时的时间戳）',
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='认证token 黑名单';
