package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.MathPlan;
import com.wusi.reimbursement.mapper.MathPlanMapper;
import com.wusi.reimbursement.service.MathPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wusi
 * @date 2021-01-18 13:28:48
 **/
@Service
public class MathPlanServiceImpl extends BaseMybatisServiceImpl<MathPlan,Long> implements MathPlanService {

    @Autowired
    private MathPlanMapper mathPlanMapper;


    @Override
    protected BaseMapper<MathPlan, Long> getBaseMapper() {
        return mathPlanMapper;
    }
}
