package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.OnlineProduct;
import com.wusi.reimbursement.mapper.OnlineProductMapper;
import com.wusi.reimbursement.service.OnlineProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Java
 * @date 2022-11-23 10:54:22
 **/
@Service
public class OnlineProductServiceImpl extends BaseMybatisServiceImpl<OnlineProduct,Long> implements OnlineProductService {

    @Autowired
    private OnlineProductMapper onlineProductMapper;


    @Override
    protected BaseMapper<OnlineProduct, Long> getBaseMapper() {
        return onlineProductMapper;
    }
}
