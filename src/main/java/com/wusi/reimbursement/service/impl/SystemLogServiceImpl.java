package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.SystemLog;
import com.wusi.reimbursement.mapper.SystemLogMapper;
import com.wusi.reimbursement.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-05-29 15:06:58
 **/
@Service
public class SystemLogServiceImpl extends BaseMybatisServiceImpl<SystemLog,Long> implements SystemLogService {

    @Autowired
    private SystemLogMapper systemLogMapper;


    @Override
    protected BaseMapper<SystemLog, Long> getBaseMapper() {
        return systemLogMapper;
    }
}
