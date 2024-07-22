package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.LureSell;
import com.wusi.reimbursement.mapper.LureSellMapper;
import com.wusi.reimbursement.service.LureSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Java
 * @date 2024-07-21 15:55:38
 **/
@Service
public class LureSellServiceImpl extends BaseMybatisServiceImpl<LureSell,Long> implements LureSellService {

    @Autowired
    private LureSellMapper lureSellMapper;


    @Override
    protected BaseMapper<LureSell, Long> getBaseMapper() {
        return lureSellMapper;
    }
}
