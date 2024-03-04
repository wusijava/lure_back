package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.Address;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @date 2020-12-31 10:31:19
 **/
@Mapper
public interface AddressMapper extends BaseMapper<Address,Long> {


}
