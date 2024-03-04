package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.SsqHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2021-01-12 16:22:33
 **/
@Mapper
public interface SsqHistoryMapper extends BaseMapper<SsqHistory,Long> {

List<Map<String ,Object>>  getValue(Integer count);
}
