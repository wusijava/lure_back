package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.IllegalLog;

/**
 * @author Java
 * @date 2024-07-22 16:28:39
 **/
public interface IllegalLogService extends BaseService<IllegalLog,Long> {

    void saveIllegalLog(String uid,String userName,String content,Integer state,Integer source,String traceId,Integer type,Integer reason);


    IllegalLog selectByTraceId(String trace_id);
}
