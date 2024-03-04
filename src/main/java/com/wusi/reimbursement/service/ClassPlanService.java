package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.ClassPlan;
import com.wusi.reimbursement.entity.Ssq;
import com.wusi.reimbursement.query.ClassPlanQuery;
import com.wusi.reimbursement.query.SsqQuery;

import java.util.List;

/**
 * @author admin
 * @date 2021-04-30 11:20:46
 **/
public interface ClassPlanService extends BaseService<ClassPlan,Long> {
    List<ClassPlan> queryListByParam(ClassPlanQuery query);
    long querycount(ClassPlanQuery query);

    ClassPlan queryLast();
}
