package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.ClassPlan;
import com.wusi.reimbursement.mapper.ClassPlanMapper;
import com.wusi.reimbursement.query.ClassPlanQuery;
import com.wusi.reimbursement.service.ClassPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @date 2021-04-30 11:20:46
 **/
@Service
public class ClassPlanServiceImpl extends BaseMybatisServiceImpl<ClassPlan,Long> implements ClassPlanService {

    @Autowired
    private ClassPlanMapper classPlanMapper;


    @Override
    protected BaseMapper<ClassPlan, Long> getBaseMapper() {
        return classPlanMapper;
    }

    @Override
    public List<ClassPlan> queryListByParam(ClassPlanQuery query) {
        return classPlanMapper.queryListByParam(query);
    }

    @Override
    public long querycount(ClassPlanQuery query) {
        return classPlanMapper.querycount(query);
    }

    @Override
    public ClassPlan queryLast() {
        return classPlanMapper.queryLast();
    }
}
