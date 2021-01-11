package com.dili.card.common.provider;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.pay.PayBankDto;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.type.CardFaceType;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * @description： 
 *          卡面提供者
 * @author ：WangBo
 * @time ：2020年9月9日下午4:55:39
 */
@Component
public class BankChannelProvider implements ValueProvider {
	private static final List<ValuePair<?>> BUFFER = new ArrayList<>();
	
	private static final Logger log = LoggerFactory.getLogger(BankChannelProvider.class);

	@Resource
	private PayRpc payRpc;
	
    static {
        CardFaceType.getAll()
                .forEach(c -> BUFFER.add(new ValuePairImpl(c.getName(), c.getCode())));
    }
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
    	UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
    	if(userTicket == null) {
    		log.warn("当前登录用户的信息为空，无法判断市场");
    	}else {
    		PayBankDto payBankDto = new PayBankDto();
    		payBankDto.setMchId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        	List<PayBankDto> data = GenericRpcResolver.resolver(payRpc.getBankChannels(payBankDto), ServiceName.PAY);
        	data.forEach(c -> BUFFER.add(new ValuePairImpl(c.getChannelName(), c.getChannelId())));
    	}
    	return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (obj == null) {
            return null;
        }
        String type = (String) obj;
        return CardFaceType.getTypeName(type);
    }
}
