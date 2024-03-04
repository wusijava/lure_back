package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.LurePositionRecord;
import com.wusi.reimbursement.mapper.LurePositionRecordMapper;
import com.wusi.reimbursement.service.LurePositionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2022-06-02 13:59:41
 **/
@Service
public class LurePositionRecordServiceImpl extends BaseMybatisServiceImpl<LurePositionRecord,Long> implements LurePositionRecordService {

    @Autowired
    private LurePositionRecordMapper lurePositionRecordMapper;


    @Override
    protected BaseMapper<LurePositionRecord, Long> getBaseMapper() {
        return lurePositionRecordMapper;
    }
}
