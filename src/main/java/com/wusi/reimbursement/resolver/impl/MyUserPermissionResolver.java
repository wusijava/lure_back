package com.wusi.reimbursement.resolver.impl;


import com.wusi.reimbursement.entity.User;
import com.wusi.reimbursement.resolver.UserPermissionResolver;
import com.wusi.reimbursement.service.UserService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.PassWordUtil;
import com.wusi.reimbursement.vo.LoginUser;
import com.wusi.reimbursement.vo.UsernamePasswordToken;
import com.wusi.reimbursement.vo.WxUsernamePasswordToken;
import com.wusi.reimbursement.wx.WxApi;
import com.wusi.reimbursement.wx.WxConfigContainer;
import com.wusi.reimbursement.wx.WxConfiguration;
import com.wusi.reimbursement.wx.WxUtils;
import com.wusi.reimbursement.wx.dto.OpenIdApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "webUserPermissionResolver")
public class MyUserPermissionResolver extends UserPermissionResolver {

    @Autowired
    private UserService userService;
    @Autowired
    private WxApi wxApi;




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
    public LoginUser getWxLoginUser(WxUsernamePasswordToken token) throws Exception {
        OpenIdApi openId = WxUtils.getOpenId(token.getCode(), WxConfiguration.getAppId(), WxConfiguration.getAppSecret());
        if(DataUtil.isEmpty(openId.getOpenid())){
            throw new UsernameAndPasswordException("openId获取失败");
        }
        User user;
        user = userService.findByUsername(openId.getOpenid());
        if(DataUtil.isEmpty(user)){
            user = userService.createUser(openId.getOpenid(), null, null);
        }
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
            if(user.getImgState().equals(1)){
                loginUser.setImg(user.getImg());
            }else{
                loginUser.setImg("https://www.picture.lureking.cn/temp/1/89jng2.jpg");
            }
            return loginUser;
        } else {
            return null;
        }
    }
}
