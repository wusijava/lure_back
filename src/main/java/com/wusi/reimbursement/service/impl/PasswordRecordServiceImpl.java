package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.PasswordRecord;
import com.wusi.reimbursement.mapper.PasswordRecordMapper;
import com.wusi.reimbursement.service.PasswordRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-04-14 14:42:14
 **/
@Service
public class PasswordRecordServiceImpl extends BaseMybatisServiceImpl<PasswordRecord,Long> implements PasswordRecordService {

    @Autowired
    private PasswordRecordMapper passwordRecordMapper;


    @Override
    protected BaseMapper<PasswordRecord, Long> getBaseMapper() {
        return passwordRecordMapper;
    }

    @Override
    public void deleteByID(Long id) {
        passwordRecordMapper.deleteByID(id);
    }
}
