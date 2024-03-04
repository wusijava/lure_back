package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.OffLineRecord;
import com.wusi.reimbursement.mapper.OffLineRecordMapper;
import com.wusi.reimbursement.service.OffLineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-12-14 15:12:44
 **/
@Service
public class OffLineRecordServiceImpl extends BaseMybatisServiceImpl<OffLineRecord,Long> implements OffLineRecordService {

    @Autowired
    private OffLineRecordMapper offLineRecordMapper;


    @Override
    protected BaseMapper<OffLineRecord, Long> getBaseMapper() {
        return offLineRecordMapper;
    }
}
