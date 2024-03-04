package com.wusi.reimbursement.service;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  wusi
 * @ CreateDate    :  2020/11/5$ 16:13$
 */
public class DebugMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib+before");
        Object Object=methodProxy.invokeSuper(o,objects);
        System.out.println("cglib+after");
        return Object;
    }
}


