package com.wusi.reimbursement.resolver.impl;


import com.wusi.reimbursement.entity.User;
import com.wusi.reimbursement.resolver.UserPermissionResolver;
import com.wusi.reimbursement.service.UserService;
import com.wusi.reimbursement.utils.PassWordUtil;
import com.wusi.reimbursement.vo.LoginUser;
import com.wusi.reimbursement.vo.UsernamePasswordToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "webUserPermissionResolver")
public class MyUserPermissionResolver extends UserPermissionResolver {

    @Autowired
    private UserService userService;



    @Override
    public Boolean hasPermission(String path,Integer type) {
        return true;
    }

    @Override
    public LoginUser getLoginUser(UsernamePasswordToken token) {
        User user = userService.findByUsername(token.getUsername());
        return getLoginUser(user);
    }

    @Override
    public LoginUser getLoginUser(String userId) {
        User user = userService.queryById(Long.valueOf(userId));
        return getLoginUser(user);
    }

    @Override
    protected Boolean comparePassWord(UsernamePasswordToken token, LoginUser loginUser) {
        String loginPass = PassWordUtil.generatePasswordSha1WithSalt(token.getPassword(), loginUser.getSalt());
        return loginPass.equals(loginUser.getPassword());
    }

    @Override
    public void checkPermission(LoginUser user, String path) {
    }

    /**
     * 获取登录用户
     *
     * @param user
     * @return
     */
    private LoginUser getLoginUser(User user) {
        if (user != null) {
            LoginUser loginUser = new LoginUser();
            loginUser.setId(String.valueOf(user.getId()));
            loginUser.setMobile(user.getMobile());
            loginUser.setNickName(user.getNickName());
            loginUser.setPassword(user.getPassword());
            loginUser.setType(user.getType());
            loginUser.setUsername(user.getUsername());
            loginUser.setUid(user.getUid());
            loginUser.setSalt(user.getSalt());
            loginUser.setStoreMarkCode(user.getStoreMarkCode());
            loginUser.setCityCode(user.getCityCode());
            loginUser.setProvinceCode(user.getProvinceCode());
            return loginUser;
        } else {
            return null;
        }
    }
}
