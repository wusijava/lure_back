package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.ClassPlan;
import com.wusi.reimbursement.query.ClassPlanQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author admin
 * @date 2021-04-30 11:20:46
 **/
@Mapper
public interface ClassPlanMapper extends BaseMapper<ClassPlan,Long> {
    List<ClassPlan> queryListByParam(ClassPlanQuery query);

    long querycount(ClassPlanQuery query);

    ClassPlan queryLast();

}
