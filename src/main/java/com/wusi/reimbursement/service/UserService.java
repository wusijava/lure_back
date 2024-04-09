package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.User;

import java.util.List;


public interface UserService extends BaseService<User,Long> {
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


    /**
     * 修改密码
     *
     * @param userId
     * @param salt
     * @param password
     * @param oldPassword
     * @param newPassword
     * @@return
     */
    String changePassword(Long userId, String salt, String password, String oldPassword, String newPassword,String nickName,String url);

    /**
     * 修改密码
     *
     * @param userId
     * @param salt
     * @param newPassword
     * @@return
     */
    String changePassword(Long userId, String salt, String newPassword);


    /**
     * 创建用户
     *
     * @param name
     * @param mobile
     */
    User createUser( String name, String mobile,String creater);

    User findByName(String name);

    List<User> queryByUidList(List<String> uid);

    User queryRepeateUser(Long id,String nickName);
}
