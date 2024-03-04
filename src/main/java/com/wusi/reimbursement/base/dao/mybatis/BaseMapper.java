package com.wusi.reimbursement.base.dao.mybatis;

import com.wusi.reimbursement.common.Identifiable;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author wusi
 * @date 2019-03-04 10:49
 **/
public interface BaseMapper<T extends Identifiable<ID>, ID extends Serializable> {

    T selectById(ID id);

    List<T> select(Map<String, Object> params);

    List<T> selectAll();

    Long selectCount(Map<String, Object> params);

    void insert(T entity);

    void insertBatch(List<T> list);

    int updateById(T entity);

    int updateByIdSelective(T entity);

    void deleteById(@Param("id") ID id);
}