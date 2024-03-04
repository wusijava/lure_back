package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.Reimbursement;

/**
 * @author admin
 * @date 2020-01-09 10:55:54
 **/
public interface ReimbursementService extends BaseService<Reimbursement,Long> {
    void delById(Long id);
    void updateByid(Reimbursement reimbursement);
}
