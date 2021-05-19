SET @firmId := 22;
-- dili_account
DELETE FROM dili_account.account_serial_record WHERE firm_id = @firmId;
--
DELETE FROM dili_account.account_card_storage WHERE firm_id = @firmId;
DELETE FROM dili_account.account_user_account WHERE firm_id = @firmId;
DELETE FROM dili_account.account_user_card WHERE firm_id = @firmId;

-- dili_card
DELETE FROM dili_card.card_account_cycle WHERE firm_id = @firmId;
DELETE FROM dili_card.card_account_cycle_detail WHERE firm_id = @firmId;
DELETE FROM dili_card.card_bank_counter WHERE firm_id = @firmId;
DELETE FROM dili_card.card_bind_bank_card WHERE firm_id = @firmId;
DELETE FROM dili_card.card_business_record WHERE firm_id = @firmId;
DELETE FROM dili_card.card_fund_consignor WHERE firm_id = @firmId;
DELETE FROM dili_card.card_fund_contract WHERE firm_id = @firmId;
DELETE FROM dili_card.card_reverse_record WHERE firm_id = @firmId;
DELETE FROM dili_card.card_user_cash WHERE firm_id = @firmId;
--
DELETE FROM dili_card.card_storage_in WHERE firm_id = @firmId;
DELETE FROM dili_card.card_storage_out WHERE firm_id = @firmId;
