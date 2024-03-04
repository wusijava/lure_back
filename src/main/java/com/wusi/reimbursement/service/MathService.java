package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.Math;
import com.wusi.reimbursement.entity.Ssq;
import com.wusi.reimbursement.query.MathQuery;
import com.wusi.reimbursement.query.SsqQuery;

import java.util.List;

/**
 * @author admin
 * @date 2021-01-15 11:55:19
 **/
public interface MathService extends BaseService<Math,Long> {

    long querycount();

    List<Math> queryListByParam(MathQuery query);
}
