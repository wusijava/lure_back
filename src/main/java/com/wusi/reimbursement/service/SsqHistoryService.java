package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.SsqHistory;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2021-01-12 16:22:33
 **/
public interface SsqHistoryService extends BaseService<SsqHistory,Long> {
    List<Map<String ,Object>> getValue(Integer count);
}
