package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.SellLog;
import com.wusi.reimbursement.mapper.SellLogMapper;
import com.wusi.reimbursement.service.SellLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-01-16 17:08:40
 **/
@Service
public class SellLogServiceImpl extends BaseMybatisServiceImpl<SellLog,Long> implements SellLogService {

    @Autowired
    private SellLogMapper sellLogMapper;


    @Override
    protected BaseMapper<SellLog, Long> getBaseMapper() {
        return sellLogMapper;
    }
}
