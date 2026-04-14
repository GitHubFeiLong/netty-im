ALTER TABLE `sys_auth_token_blacklist` ADD COLUMN `token` text NOT NULL COMMENT 'token内容' AFTER `id`;
