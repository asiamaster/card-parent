-- ----------------------------
-- 卡务系统数据模型设计
-- ----------------------------
DROP TABLE IF EXISTS `account_card_storage`;
CREATE TABLE `account_card_storage`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡片硬件标识',
  `card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡号',
  `type` tinyint(0) NOT NULL COMMENT '卡片类型码',
  `card_face` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡面（与客户类型值相同,司机园内买园外买卖）',
  `storage_in_id` bigint(0) NULL DEFAULT NULL COMMENT '入库批次ID',
  `verify_code` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `maker_version` tinyint(0) UNSIGNED NULL DEFAULT NULL COMMENT '制卡程序版本号',
  `state` tinyint(0) UNSIGNED NOT NULL COMMENT '卡片状态-激活,在用,作废',
  `creator_id` bigint(0) NOT NULL COMMENT '操作员',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作员名称',
  `notes` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `firm_id` bigint(0) NOT NULL COMMENT '商户ID',
  `firm_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_card_storage_cardNo`(`card_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38225 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '卡片仓库，所有新开卡必须来至该表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `account_login_user`;
CREATE TABLE `account_login_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` bigint(0) NOT NULL COMMENT '客户ID',
  `login_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登陆密码',
  `nick_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `head_url` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像URL',
  `state` tinyint(0) UNSIGNED NOT NULL COMMENT '正常、锁定、注销',
  `disable_state` tinyint(0) NOT NULL COMMENT '禁用状态（管理员使用:1-启用2-禁用）',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '最近登录时间',
  `version` int(0) UNSIGNED NOT NULL COMMENT '数据版本号',
  `firm_id` bigint(0) NOT NULL COMMENT '商户ID',
  `firm_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifiy_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `udx_login_user_loginName`(`login_name`) USING BTREE,
  UNIQUE INDEX `udx_login_user_customerId`(`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户电子登录账号' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `account_serial_record`;
CREATE TABLE `account_serial_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `serial_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流水号',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '操作类型-与业务办理记录保持一致',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '账户ID',
  `card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联卡号',
  `customer_id` bigint(0) NULL DEFAULT NULL COMMENT '客户ID',
  `customer_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户编号',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `customer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户身份类型',
  `action` tinyint(0) NULL DEFAULT NULL COMMENT '资金动作-收入,支出',
  `start_balance` bigint(0) NULL DEFAULT NULL COMMENT '期初余额-分',
  `amount` bigint(0) NULL DEFAULT NULL COMMENT '操作金额-分',
  `end_balance` bigint(0) NULL DEFAULT NULL COMMENT '期末余额-分',
  `trade_type` tinyint(0) NULL DEFAULT NULL COMMENT '交易类型-充值、提现、消费、转账、其他',
  `trade_channel` tinyint(0) NULL DEFAULT NULL COMMENT '交易渠道-现金、POS、网银',
  `trade_no` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易流水号',
  `fund_item` tinyint(0) NULL DEFAULT NULL COMMENT '资金项目',
  `fund_item_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资金项目名称',
  `operator_id` bigint(0) NULL DEFAULT NULL COMMENT '操作员ID',
  `operator_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员工号',
  `operator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员名称',
  `operate_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间-与支付系统保持一致',
  `notes` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `firm_id` bigint(0) NULL DEFAULT NULL COMMENT '商户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_account_serial_cardNo`(`card_no`) USING BTREE,
  INDEX `idx_account_serial_accountId`(`account_id`) USING BTREE,
  INDEX `idx_account_serial_serailNo`(`serial_no`) USING BTREE,
  INDEX `idx_account_serial_customerId`(`customer_id`) USING BTREE,
  INDEX `idx_account_serial_operatorId`(`operator_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20038 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务端账户流水' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `account_user_account`;
CREATE TABLE `account_user_account`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(0) NOT NULL COMMENT '业务主键，其它业务外键都引用该键',
  `parent_account_id` bigint(0) NULL DEFAULT NULL COMMENT '父账号ID',
  `customer_id` bigint(0) NOT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户姓名（冗余customer）',
  `customer_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户编号（冗余customer）',
  `customer_market_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户市场类型(冗余customer_market)',
  `customer_certificate_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型（冗余customer）',
  `customer_certificate_number` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号（冗余customer）',
  `customer_contacts_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话（冗余customer）',
  `fund_account_id` bigint(0) NOT NULL COMMENT '资金账号',
  `card_exist` tinyint(0) NULL DEFAULT NULL COMMENT '是否存在实体卡1-存在，2-不存在',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '账号类型-买/卖/缴费卡等',
  `usage_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户用途-交易/缴费/理财/水电费预存,多个以逗号分隔',
  `permissions` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '使用权限(充值、提现、交费等),多个以逗号分隔',
  `login_pwd` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆密码',
  `pwd_changed` tinyint(0) UNSIGNED NULL DEFAULT NULL COMMENT '强制修改密码',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '最近登陆时间',
  `secret_key` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安全密钥',
  `state` tinyint(0) UNSIGNED NOT NULL COMMENT '账户状态（暂未使用）-正常/锁定/禁用/注销',
  `disabled_state` tinyint(0) NULL DEFAULT NULL COMMENT '禁用状态1-正常2-禁用',
  `source` tinyint(0) UNSIGNED NOT NULL COMMENT '注册来源-柜台 APP',
  `version` int(0) UNSIGNED NOT NULL COMMENT '数据版本号',
  `firm_id` bigint(0) NOT NULL COMMENT '商户ID',
  `firm_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户名称',
  `creator_id` bigint(0) NULL DEFAULT NULL COMMENT '操作人员ID',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人员姓名',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_account_accountId`(`account_id`) USING BTREE,
  INDEX `idx_user_account_fundAccountId`(`fund_account_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 293 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户账户信息' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `account_user_card`;
CREATE TABLE `account_user_card`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡片硬件标识',
  `account_id` bigint(0) NOT NULL COMMENT '用户账号',
  `card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡号',
  `category` tinyint(0) UNSIGNED NOT NULL COMMENT '卡类别-园区卡 银行卡',
  `type` tinyint(0) UNSIGNED NOT NULL COMMENT '类型-主/副/临时',
  `verify_code` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `cash_pledge` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '卡片押金-分',
  `last` tinyint(0) UNSIGNED NOT NULL COMMENT '是否最近卡片-换卡时旧卡该值为0新卡为1',
  `state` tinyint(0) UNSIGNED NOT NULL COMMENT '卡片状态-正常/挂失',
  `version` int(0) UNSIGNED NOT NULL COMMENT '数据版本号',
  `creator_id` bigint(0) NOT NULL COMMENT '操作人员',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工名称-保留字段',
  `firm_id` bigint(0) NOT NULL COMMENT '商户ID',
  `firm_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户名称',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_card_cardNo`(`card_no`) USING BTREE,
  INDEX `idx_user_card_accountId`(`account_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 388 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户卡片信息（包括电子卡）' ROW_FORMAT = Dynamic;

-
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint(0) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(0) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Dynamic;
