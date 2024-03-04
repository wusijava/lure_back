package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Lure;
import com.wusi.reimbursement.mapper.LureMapper;
import com.wusi.reimbursement.service.LureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2022-06-02 11:02:15
 **/
@Service
public class LureServiceImpl extends BaseMybatisServiceImpl<Lure,Long> implements LureService {

    @Autowired
    private LureMapper lureMapper;


    @Override
    protected BaseMapper<Lure, Long> getBaseMapper() {
        return lureMapper;
    }
}
