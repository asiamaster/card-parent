-- 刷新已结账账务周期的结账时间
UPDATE card_account_cycle SET check_time = modify_time WHERE state =3
