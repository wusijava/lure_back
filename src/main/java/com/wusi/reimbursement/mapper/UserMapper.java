package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User,Long> {

    /**
     * 根据用户名查找
     *
     * @param username
     * @return
     */
    User findByUsername(String username);


    /**
     * 根据用户id查询
     *
     * @param uid
     * @return
     */
    User findByUid(String uid);
}
