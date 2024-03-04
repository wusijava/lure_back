package com.wusi.reimbursement.filter.impl;

import com.wusi.reimbursement.filter.AbstractJwtAuthenticationFilter;
import com.wusi.reimbursement.resolver.TokenStoreResolver;
import com.wusi.reimbursement.resolver.UserPermissionResolver;
import com.wusi.reimbursement.vo.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;


@WebFilter(urlPatterns = "/*")
public class AuthFilterAbstract extends AbstractJwtAuthenticationFilter {

    @Autowired
    @Qualifier(value = "webUserPermissionResolver")
    private UserPermissionResolver userPermissionResolver;

    @Autowired
    @Qualifier(value = "webTokenResolver")
    private TokenStoreResolver tokenStoreResolver;

    @Override
    public UserPermissionResolver getUserPermissionResolver() {
        return userPermissionResolver;
    }

    @Override
    public TokenStoreResolver getTokenStoreResolver() {
        return tokenStoreResolver;
    }

    @Override
    protected UsernamePasswordToken createUsernamePasswordToken(HttpServletRequest request) {
        return super.createUsernamePasswordToken(request);
    }

    @Override
    protected Boolean isLoginUrl(HttpServletRequest request) {
        return pathMatcher.match("/login", request.getServletPath());
    }

    @Override
    protected Boolean isLogoutUrl(HttpServletRequest request) {
        return pathMatcher.match("/logout", request.getServletPath());
    }

    @Override
    protected boolean isProtectedUrl(HttpServletRequest request) {
        return pathMatcher.match("/api/**", request.getServletPath())
                && !pathMatcher.match("/api/open/**", request.getServletPath())
                && !pathMatcher.match("/login", request.getServletPath());
    }

}
