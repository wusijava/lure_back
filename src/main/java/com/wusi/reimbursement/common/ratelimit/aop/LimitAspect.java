package com.wusi.reimbursement.common.ratelimit.aop;


import com.google.common.util.concurrent.RateLimiter;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.common.ratelimit.anonation.RateLimit;
import com.wusi.reimbursement.utils.IpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wusi
 * @date 2019/12/31 17:52
 */
@Component
@Aspect
public class LimitAspect {

    private static Map<String, RateLimiter> caches = new ConcurrentHashMap<>();


    @Pointcut("@annotation(com.wusi.reimbursement.common.ratelimit.anonation.RateLimit)")
    public void ServiceAspect() {

    }

    @Around("ServiceAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        RateLimit rateLimit = ((MethodSignature) signature).getMethod().getAnnotation(RateLimit.class);
        if (rateLimit != null) {
            String key = signature.getName();
            if (rateLimit.ipLimit() && getIp() != null) {
                key += getIp();
            }
            RateLimiter rateLimiter = null;
            if (!caches.containsKey(key)) {
                rateLimiter = RateLimiter.create(rateLimit.permitsPerSecond());
                caches.put(key, rateLimiter);
            } else {
                rateLimiter = caches.get(key);
            }
            Boolean flag = rateLimiter.tryAcquire();
            Object obj = null;
            try {
                if (flag) {
                    obj = joinPoint.proceed();
                } else {
                    return Response.fail("操作有点频繁啊");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return obj;
        } else {
            return Response.fail("未知错误");
        }
    }

    private String getIp() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return IpUtils.getIpAddress(request);
        } catch (Exception e) {
            return null;
        }

    }
}
