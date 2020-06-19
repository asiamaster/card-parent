package com.dili.card.dto;

/**
 * 客户查询
 * @author zhangxing
 */
public class CustomerQuery{

	/**
	 * 客户ID
	 */
	private Long id;
	
	/**
	 * 客户姓名
	 */
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
