-- dili_card
use dili_card;
-- 对帐
delete from card_account_cycle;
-- 对帐详情
delete from card_account_cycle_detail;
-- 银行存取款
delete from card_bank_counter;
-- 柜台业务办理记录
delete from card_business_record;
-- 资金委托合同-受委托人
delete from card_fund_consignor;
-- 资金委托合同
delete from card_fund_contract;
-- 采购卡片入库
delete from card_storage_in;
-- 采购卡片出库（激活）
delete from card_storage_out;
-- 出库（激活）详情
delete from card_storage_out_detail;
-- 柜员交款领款
delete from card_user_cash;
-- seata
delete from undo_log;