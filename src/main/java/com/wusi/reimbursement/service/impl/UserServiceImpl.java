package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.User;
import com.wusi.reimbursement.mapper.UserMapper;
import com.wusi.reimbursement.query.UserQuery;
import com.wusi.reimbursement.service.UserService;
import com.wusi.reimbursement.utils.PassWordUtil;
import com.wusi.reimbursement.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class UserServiceImpl extends BaseMybatisServiceImpl<User,Long> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;



    @Override
    protected BaseMapper<User, Long> getBaseMapper() {
        return userMapper;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User findByUid(String uid) {
        return userMapper.findByUid(uid);
    }

    @Override
    public String changePassword(Long userId, String salt, String password, String oldPassword, String newPassword) {
        String oldPass = PassWordUtil.generatePasswordSha1WithSalt(oldPassword, salt);
        if (!oldPass.equals(password)) {
            return "原密码错误";
        }
        String newPass = PassWordUtil.generatePasswordSha1WithSalt(newPassword, salt);
        UserQuery query = new UserQuery();
        query.setId(userId);
        query.setPassword(newPass);
        query.setPwd(newPassword);
        this.updateById(query);
        return null;
    }

    @Override
    public String changePassword(Long userId, String salt, String newPassword) {
        String newPass = PassWordUtil.generatePasswordSha1WithSalt(newPassword, salt);
        UserQuery query = new UserQuery();
        query.setId(userId);
        query.setPassword(newPass);
        this.updateById(query);
        return null;
    }


    /**
     * 创建用户
     *
     * @param mobile
     */
    @Override
    public User createUser(String name,String mobile,String creater) {

        User query = new User();
        String pwd = "123456";
        String salt = PassWordUtil.generateSalt();
        query.setCreateTime(new Date());
        query.setMobile(mobile);
        query.setUsername(mobile);
        query.setType(User.Type.USER.getCode());
        query.setUid(StringUtils.getMerchantNo());
        query.setSalt(salt);
        query.setState(User.State.OPEN.getCode());
        query.setPassword(PassWordUtil.generatePasswordSha1WithSalt(pwd,salt));
        query.setNickName(name);
        query.setCreateTime(new Date());
        query.setPwd(pwd);
        query.setRemark(creater);
        this.insert(query);
        return query;
    }

    @Override
    public User findByName(String name) {
        User query = new User();
        query.setNickName(name);
        User user = userService.queryOne(query);
        return user;
    }
}
