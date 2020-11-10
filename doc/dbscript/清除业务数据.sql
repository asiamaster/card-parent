-- dili_card
use dili_card;
-- 对帐
truncate table card_account_cycle;
-- 对帐详情
truncate table card_account_cycle_detail;
-- 银行存取款
truncate table card_bank_counter;
-- 柜台业务办理记录
truncate table card_business_record;
-- 资金委托合同-受委托人
truncate table card_fund_consignor;
-- 资金委托合同
truncate table card_fund_contract;
-- 采购卡片入库
truncate table card_storage_in;
-- 采购卡片出库（激活）
truncate table card_storage_out;
-- 出库（激活）详情
truncate table card_storage_out_detail;
-- 柜员交款领款
truncate table card_user_cash;
-- seata
truncate table undo_log;
