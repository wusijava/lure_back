package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.SsqStop;
import com.wusi.reimbursement.mapper.SsqStopMapper;
import com.wusi.reimbursement.service.SsqStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-02-01 10:06:36
 **/
@Service
public class SsqStopServiceImpl extends BaseMybatisServiceImpl<SsqStop,Long> implements SsqStopService {

    @Autowired
    private SsqStopMapper ssqStopMapper;


    @Override
    protected BaseMapper<SsqStop, Long> getBaseMapper() {
        return ssqStopMapper;
    }
}
