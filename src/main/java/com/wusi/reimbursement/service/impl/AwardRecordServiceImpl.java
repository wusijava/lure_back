package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.AwardRecord;
import com.wusi.reimbursement.mapper.AwardRecordMapper;
import com.wusi.reimbursement.service.AwardRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-04-16 15:35:29
 **/
@Service
public class AwardRecordServiceImpl extends BaseMybatisServiceImpl<AwardRecord,Long> implements AwardRecordService {

    @Autowired
    private AwardRecordMapper awardRecordMapper;


    @Override
    protected BaseMapper<AwardRecord, Long> getBaseMapper() {
        return awardRecordMapper;
    }

    @Override
    public Integer queryNum(String date) {
        return awardRecordMapper.queryNum(date);
    }
}
