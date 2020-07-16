package com.dili.card.rpc.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardManageRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;

/**
 * 卡管理rpc解析器
 * @author zhangxing
 */
@Component
public class CardManageRpcResolver {
	@Autowired
	private CardManageRpc cardManageRpc ;

	/**
     * 解挂卡片
     */
    public void unLostCard(CardRequestDto cardParam) {
    	BaseOutput<?> baseOutput = cardManageRpc.unLostCard(cardParam);
    	if (!baseOutput.isSuccess()) {
    		throw new CardAppBizException(ResultCode.DATA_ERROR, "解挂卡片失败");
		}
    }

    /**
     * 退卡
     */
    public void returnCard(CardRequestDto cardParam) {
    	BaseOutput<?> baseOutput = cardManageRpc.returnCard(cardParam);
    	if (!baseOutput.isSuccess()) {
    		throw new CardAppBizException(ResultCode.DATA_ERROR, "退卡失败：" + baseOutput.getMessage());
		}
    }

    /**
     * 重置密码
     */
    public void resetLoginPwd(CardRequestDto cardParam) {
    	BaseOutput<?> baseOutput = cardManageRpc.resetLoginPwd(cardParam);
    	if (!baseOutput.isSuccess()) {
    		throw new CardAppBizException(ResultCode.DATA_ERROR, "重置密码失败");
		}
    }

    /**
    * 换卡
    * @author miaoguoxin
    * @date 2020/7/14
    */
    public void changeCard(CardRequestDto cardParam){
    	GenericRpcResolver.resolver(cardManageRpc.changeCard(cardParam),"卡务换卡");
	}

	/**
	* 挂失
	* @author miaoguoxin
	* @date 2020/7/14
	*/
	public void reportLossCard(CardRequestDto cardParam){
		GenericRpcResolver.resolver(cardManageRpc.reportLossCard(cardParam),"卡务挂失卡片");
	}
}
