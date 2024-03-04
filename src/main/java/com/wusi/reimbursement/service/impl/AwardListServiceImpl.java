package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.AwardList;
import com.wusi.reimbursement.mapper.AwardListMapper;
import com.wusi.reimbursement.service.AwardListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-04-16 15:36:39
 **/
@Service
public class AwardListServiceImpl extends BaseMybatisServiceImpl<AwardList,Long> implements AwardListService {

    @Autowired
    private AwardListMapper awardListMapper;


    @Override
    protected BaseMapper<AwardList, Long> getBaseMapper() {
        return awardListMapper;
    }
}
