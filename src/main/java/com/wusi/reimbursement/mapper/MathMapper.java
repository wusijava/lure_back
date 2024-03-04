package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.Math;
import com.wusi.reimbursement.query.MathQuery;
import com.wusi.reimbursement.query.SsqQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author wusi
 * @date 2021-01-15 11:55:19
 **/
@Mapper
public interface MathMapper extends BaseMapper<Math,Long> {
    long querycount();

    List<Math> queryListByParam(MathQuery query);

    List<String> getResult();

}
