package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.IllegalLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Java
 * @date 2024-07-22 16:28:39
 **/
@Mapper
public interface IllegalLogMapper extends BaseMapper<IllegalLog,Long> {


    IllegalLog selectByTraceId(String traceId);
}
