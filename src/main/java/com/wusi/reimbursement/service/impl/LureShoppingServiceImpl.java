package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.LureShopping;
import com.wusi.reimbursement.mapper.LureShoppingMapper;
import com.wusi.reimbursement.service.LureShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2022-06-02 13:59:49
 **/
@Service
public class LureShoppingServiceImpl extends BaseMybatisServiceImpl<LureShopping,Long> implements LureShoppingService {

    @Autowired
    private LureShoppingMapper lureShoppingMapper;


    @Override
    protected BaseMapper<LureShopping, Long> getBaseMapper() {
        return lureShoppingMapper;
    }

    @Override
    public void delById(Long id) {
        lureShoppingMapper.deleteById(id);
    }
}
