/** 1.1.0 版本sql */
-- 增加总余额字段并调整老数据该字段值等于期初余额
ALTER TABLE card_business_record ADD COLUMN total_balance BIGINT COMMENT '总余额(包括冻结)' AFTER end_balance;
UPDATE card_business_record SET total_balance = end_balance;