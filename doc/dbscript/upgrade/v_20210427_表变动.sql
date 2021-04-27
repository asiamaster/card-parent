-- 创建etc表
CREATE TABLE `card_bind_etc` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `account_id` bigint(20) NOT NULL COMMENT '卡账户id',
                                 `card_no` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '关联卡号',
                                 `customer_id` bigint(20) NOT NULL COMMENT '客户id',
                                 `hold_name` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '持卡人名称',
                                 `plate_no` varchar(12) COLLATE utf8_unicode_ci NOT NULL COMMENT '车牌号',
                                 `license_no` varchar(25) COLLATE utf8_unicode_ci NOT NULL COMMENT '免密协议号',
                                 `operator_id` bigint(20) NOT NULL COMMENT '操作员id',
                                 `operator_name` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '操作员名称',
                                 `description` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
                                 `firm_id` bigint(20) NOT NULL COMMENT '市场id',
                                 `firm_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '市场名称',
                                 `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1：绑定 0：解绑',
                                 `create_time` datetime NOT NULL COMMENT '创建时间',
                                 `modify_time` datetime NOT NULL COMMENT '修改时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
