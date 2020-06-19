package com.dili.card.service.impl;

import com.dili.card.dao.IUserLogDao;
import com.dili.card.service.IUserLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户操作记录service实现类
 */
@Service
public class UserLogServiceImpl implements IUserLogService {

    @Resource
    private IUserLogDao userLogDao;
}
