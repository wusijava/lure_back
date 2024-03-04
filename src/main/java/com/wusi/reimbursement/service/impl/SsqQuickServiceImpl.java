package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.SsqQuick;
import com.wusi.reimbursement.mapper.SsqQuickMapper;
import com.wusi.reimbursement.service.SsqQuickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-01-11 17:45:47
 **/
@Service
public class SsqQuickServiceImpl extends BaseMybatisServiceImpl<SsqQuick,Long> implements SsqQuickService {

    @Autowired
    private SsqQuickMapper ssqQuickMapper;


    @Override
    protected BaseMapper<SsqQuick, Long> getBaseMapper() {
        return ssqQuickMapper;
    }
}
