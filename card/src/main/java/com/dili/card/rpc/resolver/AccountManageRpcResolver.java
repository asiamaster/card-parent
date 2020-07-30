package com.dili.card.rpc.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.rpc.AccountManageRpc;
import com.dili.card.type.ServiceType;

/**
 * 卡账户管理rpc解析器
 * @author zhangxing
 */
@Component
public class AccountManageRpcResolver {
	@Autowired
	private AccountManageRpc accountManageRpc ;

	/**
     * 冻结卡账户
     */
    public void frozen(CardRequestDto cardParam) {
    	GenericRpcResolver.resolver(accountManageRpc.frozen(cardParam), ServiceType.ACCOUNT_SERVCIE.getName());
    }

    /**
     * 解冻卡账户
     */
    public void unfrozen(CardRequestDto cardParam) {
    	GenericRpcResolver.resolver(accountManageRpc.unfrozen(cardParam), ServiceType.ACCOUNT_SERVCIE.getName());
    }

}
