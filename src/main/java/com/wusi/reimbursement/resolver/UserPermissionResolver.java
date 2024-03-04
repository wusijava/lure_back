package com.wusi.reimbursement.resolver;

import com.wusi.reimbursement.vo.LoginUser;
import com.wusi.reimbursement.vo.UsernamePasswordToken;
import lombok.Data;

public abstract class UserPermissionResolver {

    /**
     * 获取权限
     *
     * @param path
     * @param type
     * @return
     */
    public abstract Boolean hasPermission(String path,Integer type);

    /**
     * 获取登录用户
     *
     * @param token
     * @return
     */
    public abstract LoginUser getLoginUser(UsernamePasswordToken token);

    /**
     * 获取登录用户
     *
     * @param userId
     * @return
     */
    public abstract LoginUser getLoginUser(String userId);

    public LoginUser doAuthInfo(UsernamePasswordToken token) {
        LoginUser loginUser = getLoginUser(token);
        if (loginUser != null && comparePassWord(token, loginUser)) {
            return loginUser;
        } else {
            throw new UsernameAndPasswordException("用户名密码错误");
        }
    }

    public LoginUser doAuthInfo(String userId) {
        return getLoginUser(userId);
    }

    public void checkPermission(LoginUser user, String path) {

    }

    protected Boolean comparePassWord(UsernamePasswordToken token, LoginUser loginUser) {
        return token.getPassword().equals(loginUser.getPassword());
    }

    @Data
    public static class AuthenticationException extends RuntimeException {
        private Integer code;

        public AuthenticationException() {
        }

        public AuthenticationException(String message) {
            super(message);
        }

        public AuthenticationException(Integer code, String message) {
            super(message);
            this.code = code;
        }

        public AuthenticationException(Throwable cause) {
            super(cause);
        }

        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }

        public AuthenticationException(Integer code, String message, Throwable cause) {
            super(message, cause);
            this.code = code;
        }
    }

    public static class UsernameAndPasswordException extends RuntimeException {
        public UsernameAndPasswordException() {
        }

        public UsernameAndPasswordException(String message) {
            super(message);
        }

        public UsernameAndPasswordException(Throwable cause) {
            super(cause);
        }

        public UsernameAndPasswordException(String message, Throwable cause) {
            super(message, cause);
        }
    }


    public static class AuthorizationException extends RuntimeException {
        public AuthorizationException() {
        }

        public AuthorizationException(String message) {
            super(message);
        }

        public AuthorizationException(Throwable cause) {
            super(cause);
        }

        public AuthorizationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
