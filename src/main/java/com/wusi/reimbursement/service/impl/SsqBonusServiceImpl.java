package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.SsqBonus;
import com.wusi.reimbursement.mapper.SsqBonusMapper;
import com.wusi.reimbursement.service.SsqBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-01-05 10:19:34
 **/
@Service
public class SsqBonusServiceImpl extends BaseMybatisServiceImpl<SsqBonus,Long> implements SsqBonusService {

    @Autowired
    private SsqBonusMapper ssqBonusMapper;


    @Override
    protected BaseMapper<SsqBonus, Long> getBaseMapper() {
        return ssqBonusMapper;
    }
}
