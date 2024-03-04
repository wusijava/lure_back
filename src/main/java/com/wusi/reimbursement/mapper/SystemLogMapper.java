package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @date 2020-05-29 15:06:58
 **/
@Mapper
public interface SystemLogMapper extends BaseMapper<SystemLog,Long> {


}
