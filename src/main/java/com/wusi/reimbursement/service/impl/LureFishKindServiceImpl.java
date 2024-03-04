package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.LureFishKind;
import com.wusi.reimbursement.mapper.LureFishKindMapper;
import com.wusi.reimbursement.service.LureFishKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2022-06-02 13:59:32
 **/
@Service
public class LureFishKindServiceImpl extends BaseMybatisServiceImpl<LureFishKind,Long> implements LureFishKindService {

    @Autowired
    private LureFishKindMapper lureFishKindMapper;


    @Override
    protected BaseMapper<LureFishKind, Long> getBaseMapper() {
        return lureFishKindMapper;
    }
}
