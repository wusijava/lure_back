package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.MonitorRecord;
import com.wusi.reimbursement.mapper.MonitorRecordMapper;
import com.wusi.reimbursement.service.MonitorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-12-26 10:58:15
 **/
@Service
public class MonitorRecordServiceImpl extends BaseMybatisServiceImpl<MonitorRecord,Long> implements MonitorRecordService {

    @Autowired
    private MonitorRecordMapper monitorRecordMapper;


    @Override
    protected BaseMapper<MonitorRecord, Long> getBaseMapper() {
        return monitorRecordMapper;
    }
}
