-- 充正
 DROP TABLE IF EXISTS `dili_card`.`card_reverse_record`;
CREATE TABLE `dili_card`.`card_reverse_record`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `reverse_id` bigint(20) NOT NULL COMMENT '冲正业务id',
    `serial_no` varchar(32) NOT NULL COMMENT '关联的冲正流水号',
    `cycle_no` bigint(20) NOT NULL COMMENT '关联的冲正账务周期号(冗余)' ,
    `biz_trade_channel` tinyint(1) NOT NULL COMMENT '对应的原业务交易渠道(冗余)' ,
    `biz_serial_no` varchar(32) NOT NULL COMMENT '对应的业务流水号',
    `biz_type` tinyint(1) NOT NULL COMMENT '对应的业务类型，见枚举OperateType',
    `amount` bigint(20) NOT NULL COMMENT '冲正金额（区分正负），单位：分',
    `in_acc_change_amount` bigint(20) NULL COMMENT '园区收益账户变动金额（区分正负），单位：分',
    `operator_id` bigint(20) NOT NULL COMMENT '操作员id',
    `operator_no` varchar(50) NOT NULL COMMENT '操作员工号',
    `operator_name` varchar(20) NOT NULL COMMENT '操作员名字',
    `firm_id` bigint(20) NOT NULL COMMENT '市场id',
    `firm_name` varchar(40) NOT NULL COMMENT '市场名称',
    `create_time` datetime(0) NULL COMMENT '创建时间',
    `modify_time` datetime(0) NULL COMMENT '修改时间',
    `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '标记，1：删除 0：正常',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uni_reverse_id`(`reverse_id`) USING BTREE COMMENT '冲正唯一id',
    INDEX `inx_biz_serial_no`(`biz_serial_no`) USING BTREE COMMENT '业务号',
    INDEX `inx_serial_no`(`serial_no`) USING BTREE COMMENT '关联业务号'
) COMMENT = '冲正记录表';

 -- 银行存取款
 DROP TABLE IF EXISTS `dili_card`.`card_bank_counter`;
 CREATE TABLE `dili_card`.`card_bank_counter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` tinyint(3) unsigned NOT NULL COMMENT '动作-存款 取款',
  `amount` bigint(20) NOT NULL COMMENT '操作金额-分',
  `status` tinyint(3) unsigned NOT NULL COMMENT '状态-新建 封存',
  `serial_no` varchar(30) NOT NULL COMMENT '银行操作流水号',
  `apply_time` datetime DEFAULT NULL COMMENT '实际存取款时间',
  `operator_id` bigint(20) NOT NULL COMMENT '操作人员ID',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人员名称',
  `firm_id` bigint(20) NOT NULL COMMENT '市场id',
  `firm_name` varchar(20) NOT NULL COMMENT '市场名称',
  `description` varchar(250) DEFAULT NULL COMMENT '备注',
  `created_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `created_user_name` varchar(20) DEFAULT NULL COMMENT '创建人姓名',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_bank_counter_employeeId` (`operator_id`,`action`) USING BTREE,
  KEY `idx_bank_counter_serialNo` (`serial_no`) USING BTREE,
  KEY `idx_bank_counter_institutionCode` (`firm_id`) USING BTREE,
  KEY `idx_bank_counter_applyTime` (`apply_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1  COMMENT='银行存取款';

-- 园区卡账户绑定银行卡
 DROP TABLE IF EXISTS `dili_card`.`card_bind_bank_card`;
CREATE TABLE `dili_card`.`card_bind_bank_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bank_account_type` tinyint(3) unsigned NOT NULL COMMENT '账户类型-个人账户 对公账户',
  `account_id` bigint(20) DEFAULT NULL COMMENT '园区卡账号（市场帐户绑定没有园区卡帐号）',
  `fund_account_id` bigint(20) NOT NULL COMMENT '资金账号ID',
  `bank_name` varchar(50) DEFAULT NULL COMMENT '银行名称',
  `bank_type` int(20) NOT NULL COMMENT '银行类型-工商银行',
  `bank_no` varchar(30) NOT NULL COMMENT '银行卡号/对公账号',
  `opening_bank` varchar(50) DEFAULT NULL COMMENT '开户行如:成都XX银行XX支行',
  `opening_bank_num` varchar(30) DEFAULT NULL COMMENT '开户行编码',
  `name` varchar(60) NOT NULL COMMENT '姓名',
  `status` tinyint(3) unsigned NOT NULL COMMENT '绑定状态',
  `firm_id` bigint(20) DEFAULT NULL COMMENT '市场ID',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '员工ID',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '员工名称-保留字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(250) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_bank_card_accountId` (`account_id`,`bank_account_type`) USING BTREE,
  KEY `idx_bank_card_fundAccountId` (`fund_account_id`,`bank_account_type`) USING BTREE,
  KEY `idx_bank_card_bankNo` (`bank_no`,`bank_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8  COMMENT='园区账户绑定银行卡';

ALTER TABLE `dili_card`.`card_business_record`
ADD COLUMN `hold_name` varchar(40) NULL COMMENT '持卡人姓名' AFTER `customer_name`;

ALTER TABLE `dili_card`.`card_account_cycle` 
MODIFY COLUMN `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT FIRST;

ALTER TABLE `dili_card`.`card_business_record`
    MODIFY COLUMN `attach` text  NULL COMMENT '附加内容-存储不太重要的内容，否则请扩充该表字段' ;

INSERT INTO `uap`.`data_dictionary_value` (`dd_code`,`order_number`, `name`, `code`, `description`, `created`, `modified`, `state`) VALUES ('log_business_type', 40, '卡账户权限设置', '24', '卡账户权限设置', now(), NOW(), 1);

-- 刷新持卡人数据
update card_business_record set hold_name = customer_name where hold_name is null;

