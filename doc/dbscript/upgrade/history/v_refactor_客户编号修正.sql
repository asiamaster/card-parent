-- 同步card_business_record(交易记录表)客户编号
UPDATE `dili_card`.`card_business_record` a
    INNER JOIN `dili-customer`.`customer` b ON a.customer_id = b.id
SET a.customer_no = b.`code`;

-- 同步account_serial_record(操作记录表)客户编号
UPDATE `dili_account`.`account_serial_record` a
    INNER JOIN `dili-customer`.`customer` b ON a.customer_id = b.id
SET a.customer_no = b.`code`;

-- 同步account_user_account(账号表)客户编号
UPDATE `dili_account`.`account_user_account` a
    INNER JOIN `dili-customer`.`customer` b ON a.customer_id = b.id
SET a.customer_code = b.`code`;
