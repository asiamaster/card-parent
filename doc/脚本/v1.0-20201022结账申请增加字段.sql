ALTER TABLE `card_user_cash` ADD COLUMN `settled` tinyint(2) DEFAULT 1 COMMENT '结账申请创建为异常 0  平账后变正常1' after `state`;
update card_user_cash set settled = 0 where notes = '结账交款记录' and state = 1;