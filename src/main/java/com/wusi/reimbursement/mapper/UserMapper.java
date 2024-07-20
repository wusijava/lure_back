package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


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

    List<User> queryByUidList(@Param("uid") List<String> uid);

    User queryRepeateUser(@Param("id") Long id, @Param("nickName") String nickName);

    User selectByTraceId(String traceId);
}
