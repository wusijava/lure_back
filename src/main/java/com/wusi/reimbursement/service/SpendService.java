package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.Spend;

/**
 * @author admin
 * @date 2020-01-21 15:45:02
 **/
public interface SpendService extends BaseService<Spend,Long> {
    void delById(Long id);
}
