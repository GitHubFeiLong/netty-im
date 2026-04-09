/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 127.0.0.1:3306
 Source Schema         : im

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 09/04/2026 20:59:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for im_conv
-- ----------------------------
DROP TABLE IF EXISTS `im_conv`;
CREATE TABLE `im_conv`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `type` tinyint(4) NOT NULL COMMENT '会话类型（1-单聊，2-群聊）',
  `user1_id` bigint(11) NOT NULL COMMENT '单聊时：用户1 ID（与user2_id组合唯一，需满足user1_id < user2_id）',
  `user2_id` bigint(11) NULL DEFAULT NULL COMMENT '单聊时：用户2 ID（单聊必填，群聊为空）',
  `group_id` bigint(11) NULL DEFAULT NULL COMMENT '群聊时：关联group_id（群聊必填，单聊为空）',
  `last_msg_id` bigint(11) NULL DEFAULT NULL COMMENT '最后一条消息ID',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_single_conv`(`type`, `user1_id`, `user2_id`) USING BTREE,
  INDEX `idx_group_conv`(`type`, `group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'im会话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of im_conv
-- ----------------------------

-- ----------------------------
-- Table structure for im_conv_user
-- ----------------------------
DROP TABLE IF EXISTS `im_conv_user`;
CREATE TABLE `im_conv_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `conv_id` bigint(20) NOT NULL COMMENT '会话ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `unread_count` int(11) NOT NULL COMMENT '该用户在此会话的未读消息数（初始0，新消息+1，阅读后清零）',
  `last_read_id` bigint(20) NULL DEFAULT NULL COMMENT '该用户最后阅读的消息ID（用于定位未读起点）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_conv_user`(`conv_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'im用户会话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of im_conv_user
-- ----------------------------

-- ----------------------------
-- Table structure for im_friend
-- ----------------------------
DROP TABLE IF EXISTS `im_friend`;
CREATE TABLE `im_friend`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `friend_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '好友ID',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_friend`(`user_id`, `friend_id`) USING BTREE COMMENT '用户好友唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户好友表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of im_friend
-- ----------------------------
INSERT INTO `im_friend` VALUES (1, 1, '1', '2026-04-08 11:54:54', '2026-04-08 11:54:54');

-- ----------------------------
-- Table structure for im_message
-- ----------------------------
DROP TABLE IF EXISTS `im_message`;
CREATE TABLE `im_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `conv_id` bigint(20) NOT NULL COMMENT '所属会话ID',
  `sender_id` bigint(20) NOT NULL COMMENT '发送者ID',
  `receiver_type` tinyint(4) NOT NULL COMMENT '接收者类型（1-用户，2-群），非空',
  `receiver_id` bigint(20) NOT NULL COMMENT '接收者ID（用户ID或群ID，与receiver_type对应）',
  `msg_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息类型（video  order  image  text），非空',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `send_time` datetime(0) NOT NULL COMMENT '发送时间（精确到毫秒）',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '消息状态（0-发送中，1-已送达服务器，2-已送达接收方，3-已读），默认0',
  `extra` json NULL COMMENT '扩展字段（如语音时长、文件大小、@用户列表）',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_conv_msg`(`conv_id`, `send_time`) USING BTREE,
  INDEX `idx_receiver_msg`(`receiver_type`, `receiver_id`, `send_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'im消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of im_message
-- ----------------------------

-- ----------------------------
-- Table structure for im_user
-- ----------------------------
DROP TABLE IF EXISTS `im_user`;
CREATE TABLE `im_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `status` tinyint(255) NULL DEFAULT 0 COMMENT '用户状态（0-离线，1-在线，2-忙碌，3-隐身），默认0',
  `version` int(11) NULL DEFAULT 0 COMMENT '版本号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除状态（0-未删除；>0删除时的时间戳）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'im账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of im_user
-- ----------------------------
INSERT INTO `im_user` VALUES (1, '2', '1', NULL, NULL, 1, 1, 0, '2026-03-31 16:01:29', '2026-04-09 20:58:22');

SET FOREIGN_KEY_CHECKS = 1;
