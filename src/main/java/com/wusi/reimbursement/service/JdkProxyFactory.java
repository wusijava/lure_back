package com.wusi.reimbursement.service;

import java.lang.reflect.Proxy;

/**
 * @ Description   :  动态代理工厂类
 * @ Author        :  wusi
 * @ CreateDate    :  2020/11/5$ 15:53$
 */
public class JdkProxyFactory {
    public static Object getProxy(Object target){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new DebugInvocationHandler(target));
    }
}
