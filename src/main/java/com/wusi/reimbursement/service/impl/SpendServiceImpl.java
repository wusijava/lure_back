package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Spend;
import com.wusi.reimbursement.mapper.SpendMapper;
import com.wusi.reimbursement.service.SpendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-01-21 15:45:02
 **/
@Service
public class SpendServiceImpl extends BaseMybatisServiceImpl<Spend,Long> implements SpendService {

    @Autowired
    private SpendMapper spendMapper;


    @Override
    protected BaseMapper<Spend, Long> getBaseMapper() {
        return spendMapper;
    }

    @Override
    public void delById(Long id) {
        spendMapper.delById(id);
    }
}
