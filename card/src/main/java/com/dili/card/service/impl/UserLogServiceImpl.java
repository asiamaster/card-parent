package com.dili.card.service.impl;

import com.dili.card.dao.IUserLogDao;
import com.dili.card.entity.UserLogDo;
import com.dili.card.rpc.UidRpc;
import com.dili.card.service.ICustomerService;
import com.dili.card.service.IUserLogService;
import com.dili.customer.sdk.domain.Customer;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
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
    private UidRpc uidRpc;
    @Resource
    private ICustomerService customerService;


    @Override
    public void setSerialNo(UserLogDo userLog) {
        BaseOutput<String> baseOutput = uidRpc.bizNumber("card_operate_no");
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", "获取操作流水失败");
        }
        userLog.setSerialNo(baseOutput.getData());
    }

    @Override
    public void setCustomerInfo(Long customerId, Long firmId, UserLogDo userLog) {
        Customer customer = customerService.getWithNotNull(customerId, firmId);
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
