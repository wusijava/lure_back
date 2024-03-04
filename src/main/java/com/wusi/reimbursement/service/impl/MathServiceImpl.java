package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Math;
import com.wusi.reimbursement.mapper.MathMapper;
import com.wusi.reimbursement.query.MathQuery;
import com.wusi.reimbursement.query.SsqQuery;
import com.wusi.reimbursement.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @date 2021-01-15 11:55:19
 **/
@Service
public class MathServiceImpl extends BaseMybatisServiceImpl<Math,Long> implements MathService {

    @Autowired
    private MathMapper mathMapper;


    @Override
    protected BaseMapper<Math, Long> getBaseMapper() {
        return mathMapper;
    }

    @Override
    public long querycount() {
        return mathMapper.querycount();
    }

    @Override
    public List<Math> queryListByParam(MathQuery query) {
        return mathMapper.queryListByParam(query);
    }
}
