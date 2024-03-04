package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Address;
import com.wusi.reimbursement.mapper.AddressMapper;
import com.wusi.reimbursement.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2020-12-31 10:31:19
 **/
@Service
public class AddressServiceImpl extends BaseMybatisServiceImpl<Address,Long> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;


    @Override
    protected BaseMapper<Address, Long> getBaseMapper() {
        return addressMapper;
    }
}
