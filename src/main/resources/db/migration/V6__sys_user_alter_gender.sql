ALTER TABLE `sys_user` MODIFY COLUMN `gender` tinyint(4) NULL DEFAULT 0 COMMENT '性别(1-男 2-女 0-保密)' AFTER `email`;
