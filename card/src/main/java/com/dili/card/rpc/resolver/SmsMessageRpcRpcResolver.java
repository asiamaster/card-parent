package com.dili.card.rpc.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.session.SessionContext;
import com.diligrp.message.sdk.domain.input.MessageInfoInput;
import com.diligrp.message.sdk.rpc.SmsMessageRpc;

/**
 * @description：短信中心统一处理
 *          
 * @author     ：WangBo
 * @time       ：2021年1月22日上午11:27:37
 */
public class SmsMessageRpcRpcResolver {
	
	private static final Logger log = LoggerFactory.getLogger(SmsMessageRpcRpcResolver.class);

	@Autowired
    private  SmsMessageRpc smsMessageRpc; 

	/**
     * 发送短信
     */
    public void frozen(String cellphone, String sceneCode,String templateCode) {
    	BaseOutput out = null;
    	try {
    		Long firmId = SessionContext.getSessionContext().getUserTicket().getFirmId();
    		MessageInfoInput message = new MessageInfoInput();
        	message.setCellphone(cellphone);
        	message.setMarketCode(firmId+"");
        	message.setSceneCode(sceneCode);   // 应用场景 
//        	message.setSystemCode(systemCode);
//        	message.setTemplateCode(templateCode); // 短信模板ID
//        	smsMessageRpc.receiveMessage(messageInfoInput)
//        	out = GenericRpcResolver.resolver(smsMessageRpc.receiveMessage(message), ServiceName.MESSAGE);
		} catch (Exception e) {
			log.error("{}[{}][{}]短信发送失败{}",cellphone,sceneCode,templateCode,JSONObject.toJSONString(out));
		}
    }

}
