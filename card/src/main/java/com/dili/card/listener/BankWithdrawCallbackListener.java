package com.dili.card.listener;

import com.alibaba.fastjson.JSON;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IFundService;
import com.dili.card.util.AssertUtils;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * rabbit mq 消息监听器
 */
@Component
public class BankWithdrawCallbackListener {

    public static final String EXCHANGE_NAME = "pipeline.callback.exchange";
    public static final String QUEUE_NAME = "pipelineCallbackQueue";

    private static final Logger LOGGER = LoggerFactory.getLogger(BankWithdrawCallbackListener.class);

    @Autowired
    private IFundService fundService;


    /**
     * 客户信息修改后，更新账户冗余信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = QUEUE_NAME, autoDelete = "false"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.DIRECT)
    ), ackMode = "MANUAL")
    public void process(Channel channel, Message message) {
        String data = new String(message.getBody(), StandardCharsets.UTF_8);
        if (StringUtils.isBlank(data)) {
            rejectMsg(channel, message.getMessageProperties().getDeliveryTag());
            return;
        }
        LOGGER.info("接收MQ消息，开始处理银行圈提回调：{}", data);

        TradeResponseDto tradeResponseDto;
        try {
            tradeResponseDto = JSON.parseObject(data, TradeResponseDto.class);
            AssertUtils.notEmpty("圈提回调缺少流水号", tradeResponseDto.getSerialNo());
        } catch (CardAppBizException e) {
            LOGGER.error("圈提参数异常:{}", e.getMessage());
            rejectMsg(channel, message.getMessageProperties().getDeliveryTag());
            return;
        } catch (Exception e) {
            LOGGER.error("deserialize json failed", e);
            rejectMsg(channel, message.getMessageProperties().getDeliveryTag());
            return;
        }

        try {
            fundService.handleBankWithdrawCallback(tradeResponseDto);
            ackMsg(channel, message.getMessageProperties().getDeliveryTag());
        } catch (Exception e) {
            LOGGER.error("业务处理失败，开始重新投递", e);
            Boolean redelivered = message.getMessageProperties().getRedelivered();
            if (redelivered) {
                // 消息已重复处理失败, 扔掉消息
                rejectMsg(channel, message.getMessageProperties().getDeliveryTag());
            } else {
                nackMsg(channel, message.getMessageProperties().getDeliveryTag());
            }
        }
    }

    /**
     *
     * @author miaoguoxin
     * @date 2020/9/16
     */
    private static void rejectMsg(Channel channel, long deliveryTag) {
        try {
            channel.basicReject(deliveryTag, false);
        } catch (IOException e) {
            LOGGER.error("reject message failed", e);
        }
    }

    /**
     *
     * @author miaoguoxin
     * @date 2020/9/16
     */
    private static void nackMsg(Channel channel, long deliveryTag) {
        try {
            channel.basicNack(deliveryTag, false, true);
        } catch (IOException e) {
            LOGGER.error("nack message failed", e);
        }
    }

    /**
     *
     * @author miaoguoxin
     * @date 2020/9/16
     */
    private static void ackMsg(Channel channel, long deliveryTag) {
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            LOGGER.error("ack message failed", e);
        }
    }
}
