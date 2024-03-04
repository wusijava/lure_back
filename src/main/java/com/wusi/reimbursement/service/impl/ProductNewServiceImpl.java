package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.ProductNew;
import com.wusi.reimbursement.mapper.ProductNewMapper;
import com.wusi.reimbursement.service.ProductNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-12-14 15:12:06
 **/
@Service
public class ProductNewServiceImpl extends BaseMybatisServiceImpl<ProductNew,Long> implements ProductNewService {

    @Autowired
    private ProductNewMapper productNewMapper;


    @Override
    protected BaseMapper<ProductNew, Long> getBaseMapper() {
        return productNewMapper;
    }
}
