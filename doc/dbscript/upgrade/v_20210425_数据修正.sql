-- 操作记录/流水记录 序列号修正
use dili_account;
create table tmp_account_serial_record_0429 like account_serial_record;
insert into tmp_account_serial_record_0429 select * from account_serial_record where type =10;
use dili_card;
create table tmp_card_business_record_0429 like card_business_record;
insert into tmp_card_business_record_0429 select * from card_business_record where type =10;

update  dili_account.tmp_account_serial_record_0429 asr
      join dili_card.tmp_card_business_record_0429 cbr on asr.account_id=cbr.account_id
      SET asr.serial_no = cbr.serial_no;

update dili_account.tmp_account_serial_record_0429 a
join dili_account.account_serial_record b on a.id =b.id
set b.serial_no=a.serial_no;

-- 解决类型冲突
ALTER TABLE `dili_card`.`card_business_record`
    MODIFY COLUMN `type` smallint(4) NULL DEFAULT NULL COMMENT '业务类型-办卡、充值、提现等' AFTER `cycle_no`;

UPDATE  dili_account.account_serial_record SET type = type + 1000 WHERE type <=36;
UPDATE  dili_card.card_business_record SET type = type +1000 WHERE type <=36;
UPDATE  dili_card.card_reverse_record SET biz_type = biz_type +1000 WHERE biz_type <=36;
