package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.AwardRecord;

/**
 * @author admin
 * @date 2021-04-16 15:35:29
 **/
public interface AwardRecordService extends BaseService<AwardRecord,Long> {
Integer queryNum(String date);
}
