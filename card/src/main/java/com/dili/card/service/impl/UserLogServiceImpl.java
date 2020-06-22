package com.dili.card.service.impl;

import com.dili.card.dao.IUserLogDao;
import com.dili.card.entity.UserLogDo;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IUserLogService;
import com.dili.card.type.BizNoType;
import com.dili.customer.sdk.domain.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户操作记录service实现类
 */
@Service
public class UserLogServiceImpl implements IUserLogService {

    @Resource
    private IUserLogDao userLogDao;
    @Resource
    private UidRpcResovler uidRpcResovler;
    @Resource
    private CustomerRpcResolver customerRpcResolver;


    @Override
    public void setSerialNo(UserLogDo userLog) {
        userLog.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
    }

    @Override
    public void setCustomerInfo(Long customerId, Long firmId, UserLogDo userLog) {
        Customer customer = customerRpcResolver.getWithNotNull(customerId, firmId);
        userLog.setCustomerId(customer.getId());
        userLog.setCustomerNo(customer.getCode());
        userLog.setCustomerName(customer.getName());
    }

    @Override
    public void setAccountCycleInfo(Long opId, UserLogDo userLog) {
        //TODO 待完成
    }

    @Transactional
    @Override
    public void saveUserLog(UserLogDo userLog) {
        userLogDao.save(userLog);
    }
}
