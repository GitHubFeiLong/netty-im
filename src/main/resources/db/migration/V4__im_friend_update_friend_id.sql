ALTER TABLE `im_friend`
    MODIFY COLUMN `friend_id` bigint(20) NOT NULL COMMENT '好友ID' AFTER `user_id`;
