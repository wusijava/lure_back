package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.Ssq;
import com.wusi.reimbursement.query.SsqQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2021-01-05 11:42:29
 **/
@Mapper
public interface SsqMapper extends BaseMapper<Ssq,Long> {

    List<Ssq> queryListByParam(SsqQuery ssqQuery);

    long querycount(String buyer);

    Map<String,Object>  getValue();

    String getCommission(String buyer);
}
