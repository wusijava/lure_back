package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.Spend;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wusi
 * @date 2020-01-21 15:45:02
 **/
@Mapper
public interface SpendMapper extends BaseMapper<Spend,Long> {

    void delById(Long id);
}
