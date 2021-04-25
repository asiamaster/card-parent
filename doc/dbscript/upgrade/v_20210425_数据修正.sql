-- 修正流水记录的serial_no
UPDATE dili_account.account_serial_record asr
    JOIN dili_card.card_business_record cbr ON TIMESTAMPDIFF(SECOND, asr.operate_time, cbr.operate_time) <= 2 AND
                                               TIMESTAMPDIFF(SECOND, asr.operate_time, cbr.operate_time) >= 0
SET asr.serial_no = cbr.serial_no
WHERE asr.type = 10
