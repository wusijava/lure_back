package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.Ssq;
import com.wusi.reimbursement.query.SsqQuery;

import java.util.List;

/**
 * @author wusi
 * @date 2021-01-05 11:42:29
 **/
public interface SsqService extends BaseService<Ssq,Long> {
    List<Ssq> queryListByParam(SsqQuery ssqQuery);
    long querycount(String buyer);
}
