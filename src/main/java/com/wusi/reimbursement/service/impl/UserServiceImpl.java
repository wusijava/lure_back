package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.entity.User;
import com.wusi.reimbursement.mapper.UserMapper;
import com.wusi.reimbursement.query.UserQuery;
import com.wusi.reimbursement.service.LureFishGetService;
import com.wusi.reimbursement.service.LureShoppingService;
import com.wusi.reimbursement.service.UserService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.PassWordUtil;
import com.wusi.reimbursement.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl extends BaseMybatisServiceImpl<User,Long> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private LureFishGetService lureFishGetService;
    @Autowired
    private LureShoppingService lureShoppingService;



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
    @Transactional(rollbackFor =Exception.class)
    public String changePassword(Long userId, String salt, String password, String oldPassword, String newPassword,String nickName,String url) {
        Integer updateFlag =0;
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        UserQuery query = new UserQuery();
        if(DataUtil.isNotEmpty(oldPassword)&&DataUtil.isNotEmpty(newPassword)){
            String oldPass = PassWordUtil.generatePasswordSha1WithSalt(oldPassword, salt);
            if (!oldPass.equals(password)) {
                return "原密码错误！";
            }
            String newPass = PassWordUtil.generatePasswordSha1WithSalt(newPassword, salt);
            query.setPassword(newPass);
            query.setPwd(newPassword);
            updateFlag++;
        }
       if(DataUtil.isNotEmpty(nickName)){
           User user = userService.queryRepeateUser(userId, nickName);
           if(DataUtil.isNotEmpty(user)){
               return "此昵称已存在！";
           }
           if(!loginUser.getNickName().equals(nickName.trim())){
               query.setNickName(nickName);
               lureFishGetService.updateNickName(loginUser.getNickName(),nickName);
               lureShoppingService.updateNickName(loginUser.getNickName(),nickName);
               updateFlag++;
           }
       }
        query.setId(userId);
        if(DataUtil.isNotEmpty(url)){
            query.setImg(url);
            updateFlag++;
        }
        if(updateFlag > 0){
            this.updateById(query);
        }
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

    @Override
    public List<User> queryByUidList(List<String> uid) {
        if(DataUtil.isEmpty(uid)){
            return null;
        }
        return userMapper.queryByUidList(uid);
    }

    @Override
    public User queryRepeateUser(Long id, String nickName) {
        return userMapper.queryRepeateUser(id,nickName);
    }
}
