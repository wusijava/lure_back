package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.RunPlan;
import com.wusi.reimbursement.mapper.RunPlanMapper;
import com.wusi.reimbursement.service.RunPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-12-24 15:56:05
 **/
@Service
public class RunPlanServiceImpl extends BaseMybatisServiceImpl<RunPlan,Long> implements RunPlanService {

    @Autowired
    private RunPlanMapper runPlanMapper;


    @Override
    protected BaseMapper<RunPlan, Long> getBaseMapper() {
        return runPlanMapper;
    }
}
