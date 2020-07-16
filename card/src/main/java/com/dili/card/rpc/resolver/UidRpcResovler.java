package com.dili.card.rpc.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.UidRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

/**
 * 编号生成
 * @author zhangxing
 *
 */
@Component
public class UidRpcResovler {
	
	@Autowired
	private UidRpc uidRpc;

    /**
     * 根据业务类型获取业务号
     * @param type
     * @return
     */
    public String bizNumber(String type) {
    	BaseOutput<String> baseOutput = uidRpc.bizNumber(type);
    	if (!baseOutput.isSuccess()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "远程调用编号生成服务失败");
		}
		return baseOutput.getData();
    }
}
