CREATE TABLE `dili_card`.`card_reverse_record`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `reverse_id` bigint(20) NOT NULL COMMENT '冲正业务id',
    `biz_serial_no` varchar(32) NOT NULL COMMENT '对应的业务流水号',
    `biz_trade_type` tinyint(1) NOT NULL COMMENT '对应的业务类型，见枚举TradeType',
    `amount` bigint(20) NOT NULL COMMENT '冲正金额（区分正负），单位：分',
    `operator_id` bigint(20) NOT NULL COMMENT '操作员id',
    `operator_no` varchar(50) NOT NULL COMMENT '操作员工号',
    `operator_name` varchar(20) NOT NULL COMMENT '操作员名字',
    `create_time` datetime(0) NULL COMMENT '创建时间',
    `modify_time` datetime(0) NULL COMMENT '修改时间',
    `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '标记，1：删除 0：正常',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uni_reverse_id`(`reverse_id`) USING BTREE COMMENT '冲正唯一id',
    INDEX `inx_serial_no`(`biz_serial_no`) USING BTREE COMMENT '业务号'
) COMMENT = '冲正记录表';

ALTER TABLE `dili_card`.`card_reverse_record`
    ADD COLUMN `firm_id` bigint(20) NOT NULL COMMENT '市场id' AFTER `operator_name`,
    ADD COLUMN `firm_name` varchar(40) NOT NULL COMMENT '市场名称' AFTER `firm_id`;

ALTER TABLE `dili_card`.`card_reverse_record`
    ADD COLUMN `serial_no` varchar(32) NOT NULL COMMENT '关联的冲正流水号' AFTER `reverse_id`,
    RENAME INDEX `inx_serial_no` TO `inx_biz_serial_no`,
    ADD INDEX `inx_serial_no`(`serial_no`) USING BTREE COMMENT '关联业务号';
