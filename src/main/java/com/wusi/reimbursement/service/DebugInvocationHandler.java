package com.wusi.reimbursement.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  wusi
 * @ CreateDate    :  2020/11/5$ 15:50$
 */
public class DebugInvocationHandler implements InvocationHandler {
    /**
     * 代理类中的真实对象
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    private final Object target;

    public DebugInvocationHandler(Object object) {
        this.target = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //调用之前添加自己的操作
        System.out.println("before++");
        Object result=method.invoke(target,args);
        System.out.println("after++");
        return null;
    }
}
