package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Housework;
import com.wusi.reimbursement.mapper.HouseworkMapper;
import com.wusi.reimbursement.service.HouseworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-12-28 15:15:07
 **/
@Service
public class HouseworkServiceImpl extends BaseMybatisServiceImpl<Housework,Long> implements HouseworkService {

    @Autowired
    private HouseworkMapper houseworkMapper;


    @Override
    protected BaseMapper<Housework, Long> getBaseMapper() {
        return houseworkMapper;
    }
}
