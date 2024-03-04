package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.OnlineStore;
import com.wusi.reimbursement.mapper.OnlineStoreMapper;
import com.wusi.reimbursement.service.OnlineStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Java
 * @date 2022-11-23 11:11:13
 **/
@Service
public class OnlineStoreServiceImpl extends BaseMybatisServiceImpl<OnlineStore,Long> implements OnlineStoreService {

    @Autowired
    private OnlineStoreMapper onlineStoreMapper;


    @Override
    protected BaseMapper<OnlineStore, Long> getBaseMapper() {
        return onlineStoreMapper;
    }
}
