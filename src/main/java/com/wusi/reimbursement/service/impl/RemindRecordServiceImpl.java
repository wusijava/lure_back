package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.RemindRecord;
import com.wusi.reimbursement.mapper.RemindRecordMapper;
import com.wusi.reimbursement.service.RemindRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-12-30 12:57:58
 **/
@Service
public class RemindRecordServiceImpl extends BaseMybatisServiceImpl<RemindRecord,Long> implements RemindRecordService {

    @Autowired
    private RemindRecordMapper remindRecordMapper;


    @Override
    protected BaseMapper<RemindRecord, Long> getBaseMapper() {
        return remindRecordMapper;
    }
}
