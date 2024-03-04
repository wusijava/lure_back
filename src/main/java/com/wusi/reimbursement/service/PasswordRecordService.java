package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.PasswordRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @author admin
 * @date 2021-04-14 14:42:14
 **/
public interface PasswordRecordService extends BaseService<PasswordRecord, Long> {
    void deleteByID(Long id);
}
