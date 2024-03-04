package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Reimbursement;
import com.wusi.reimbursement.mapper.ReimbursementMapper;
import com.wusi.reimbursement.service.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-01-09 10:55:54
 **/
@Service
public class ReimbursementServiceImpl extends BaseMybatisServiceImpl<Reimbursement,Long> implements ReimbursementService {

    @Autowired
    private ReimbursementMapper reimbursementMapper;


    @Override
    protected BaseMapper<Reimbursement, Long> getBaseMapper() {
        return reimbursementMapper;
    }

    @Override
    public void delById(Long id) {
        reimbursementMapper.delById(id);
    }

    @Override
    public void updateByid(Reimbursement reimbursement) {
        reimbursementMapper.updateByid(reimbursement);
    }
}
