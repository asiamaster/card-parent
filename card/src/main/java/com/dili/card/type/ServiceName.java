package com.dili.card.type;

import java.io.Serializable;

/**
 * @description： 
 *          微服务名称
 * @author ：WangBo
 * @time ：2020年7月7日上午10:53:46
 */
public enum ServiceName implements Serializable {
	/** 支付服务 */
	PAY("支付"),
	
	/** 账户服务 */
	ACCOUNT("账户"),
	
	/** 客户服务 */
	CUSTOMER("客户");

	private String name;

	private ServiceName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static void main(String[] args) {
		System.out.println(ServiceName.PAY);
	}
}
