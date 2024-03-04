package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.SellLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wusi
 * @date 2020-01-16 17:08:40
 **/
@Mapper
public interface SellLogMapper extends BaseMapper<SellLog,Long> {
   void deleteById(Integer id);

}
