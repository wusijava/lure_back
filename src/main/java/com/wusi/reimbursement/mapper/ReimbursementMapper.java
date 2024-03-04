package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.Reimbursement;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @date 2020-01-09 10:55:54
 **/
@Mapper
public interface ReimbursementMapper extends BaseMapper<Reimbursement,Long> {

void delById(Long id);
void updateByid(Reimbursement reimbursement);
}
