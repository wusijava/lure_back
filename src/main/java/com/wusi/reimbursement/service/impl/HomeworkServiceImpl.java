package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Homework;
import com.wusi.reimbursement.mapper.HomeworkMapper;
import com.wusi.reimbursement.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-12-24 15:55:38
 **/
@Service
public class HomeworkServiceImpl extends BaseMybatisServiceImpl<Homework,Long> implements HomeworkService {

    @Autowired
    private HomeworkMapper homeworkMapper;


    @Override
    protected BaseMapper<Homework, Long> getBaseMapper() {
        return homeworkMapper;
    }
}
