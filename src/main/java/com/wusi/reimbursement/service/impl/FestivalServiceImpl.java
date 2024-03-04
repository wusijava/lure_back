package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Festival;
import com.wusi.reimbursement.mapper.FestivalMapper;
import com.wusi.reimbursement.service.FestivalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-03-05 15:46:30
 **/
@Service
public class FestivalServiceImpl extends BaseMybatisServiceImpl<Festival,Long> implements FestivalService {

    @Autowired
    private FestivalMapper festivalMapper;


    @Override
    protected BaseMapper<Festival, Long> getBaseMapper() {
        return festivalMapper;
    }
}
