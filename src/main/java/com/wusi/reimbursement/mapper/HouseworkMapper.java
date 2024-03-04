package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.Housework;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wusi
 * @date 2020-12-28 15:15:07
 **/
@Mapper
public interface HouseworkMapper extends BaseMapper<Housework,Long> {


}
