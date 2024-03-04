package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @date 2020-01-08 16:57:27
 **/
@Mapper
public interface RoleMapper extends BaseMapper<Role,Long> {


}
