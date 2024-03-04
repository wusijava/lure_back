package com.wusi.reimbursement.filter;



import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.resolver.TokenStoreResolver;
import com.wusi.reimbursement.resolver.UserPermissionResolver;
import com.wusi.reimbursement.utils.JwtUtil;
import com.wusi.reimbursement.vo.LoginUser;
import com.wusi.reimbursement.vo.UsernamePasswordToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public abstract class AbstractJwtAuthenticationFilter extends OncePerRequestFilter {

    protected static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        if (request.getMethod().equalsIgnoreCase("options")) {
            filterChain.doFilter(request, response);
        } else {
            LoginUser user = null;
            try {
                if (isProtectedUrl(request)) {
                    request = JwtUtil.validateTokenAndAddUserIdToHeader(request, getTokenStoreResolver());
                    user = getUserPermissionResolver().getLoginUser(JwtUtil.getCurrentUserId(request));
                    if (user != null){
                       Boolean flag = getUserPermissionResolver().hasPermission(request.getServletPath(),user.getType());
                        if (!flag){
                            throw new UserPermissionResolver.AuthorizationException("没有该权限");
                        }
                    }
                }
                if (isLoginUrl(request)) {
                    user = getUserPermissionResolver().doAuthInfo(createUsernamePasswordToken(request));
                }
                if (user != null) {
                    RequestContext.RequestUser requestUser = new RequestContext.RequestUser();
                    requestUser.setUsername(user.getUsername());
                    requestUser.setId(user.getId());
                    requestUser.setNickName(user.getNickName());
                    requestUser.setMobile(user.getMobile());
                    requestUser.setType(user.getType());
                    requestUser.setUid(user.getUid());
                    requestUser.setSalt(user.getSalt());
                    requestUser.setPassword(user.getPassword());
                    requestUser.setStoreMarkCode(user.getStoreMarkCode());
                    requestUser.setCityCode(user.getCityCode());
                    requestUser.setProvinceCode(user.getProvinceCode());
                    RequestContext.setCurrentUser(requestUser);
                    if (!isLogoutUrl(request)) {
                        JwtUtil.refreshAndAddTokenToResponseHeader(request, response, user.getId(), user.getUsername(), getTokenStoreResolver());
                    }
                }
            } catch (UserPermissionResolver.UsernameAndPasswordException e) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":\"40010\",\"msg\":\"" + e.getMessage() + "\"}");
                return;
            } catch (UserPermissionResolver.AuthenticationException e) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                if (e.getCode() != null) {
                    response.getWriter().write("{\"code\":" + e.getCode() + ",\"msg\":\"" + e.getMessage() + "\"}");
                } else {
                    response.getWriter().write("{\"code\":\"40015\",\"msg\":\"" + e.getMessage() + "\"}");
                }
                return;
            } catch (UserPermissionResolver.AuthorizationException e) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":\"40100\",\"msg\":\"" + e.getMessage() + "\"}");
                return;
            } catch (Exception e) {
                log.error("Jwt鉴权出现异常：{}", e);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":\"40015\",\"msg\":\"登录信息已过期\"}");
                return;
            }
            filterChain.doFilter(request, response);
            //后置请求,请求成功后判断是否登出请求，如果是则清除jwtToken,如果是有登录用户的则刷新jwtToken
            try {
                if (isLogoutUrl(request)) {
                    JwtUtil.logoutToken(request, getTokenStoreResolver());
                }
            } catch (Exception e) {
                log.error("exception:{}", e);
            }
        }
    }


    protected UsernamePasswordToken createUsernamePasswordToken(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return new UsernamePasswordToken(username, password);
    }

    /**
     * 权限相关
     *
     * @return
     */
    public abstract UserPermissionResolver getUserPermissionResolver();

    /**
     * 登录相关
     *
     * @return
     */
    public abstract TokenStoreResolver getTokenStoreResolver();

    /**
     * 登录url
     *
     * @param request
     * @return
     */
    protected abstract Boolean isLoginUrl(HttpServletRequest request);

    /**
     * 登出url
     *
     * @param request
     * @return
     */
    protected abstract Boolean isLogoutUrl(HttpServletRequest request);

    /**
     * 是否为受保护的url
     *
     * @param request
     * @return
     */
    protected abstract boolean isProtectedUrl(HttpServletRequest request);



}