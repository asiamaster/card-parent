package com.dili.card.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description： 开卡业务通知MQ配置
 * 
 * @author ：WangBo
 * @time ：2020年9月2日上午10:33:16
 */
@Configuration
public class OpenCardMQConfig {

	/** openCard exchange */
	public static final String EXCHANGE = "exchange_cardWeb_openCard";
	/** queue_cardWeb_openCard_customer */
	public static final String QUEUE = "queue_cardWeb_openCard_customer";
	/** routing_cardWeb_openCard */
	public static final String ROUTING = "routing_cardWeb_openCard";

	/**
	 * queue
	 * 
	 * @return
	 */
	@Bean
	public Queue openCardCustomerQueue() {
		return new Queue(QUEUE);
	}

	/**
	 * direct
	 * 
	 * @return
	 */
	@Bean
	public DirectExchange openCardDirectExchange() {
		return new DirectExchange(EXCHANGE);
	}

	/**
	 * binding
	 * 
	 * @return
	 */
	@Bean
	public Binding openCardCustomerBinding() {
		return BindingBuilder.bind(openCardCustomerQueue()).to(openCardDirectExchange()).with(ROUTING);
	}
}
