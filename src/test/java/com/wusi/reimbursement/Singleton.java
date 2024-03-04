package com.wusi.reimbursement;
public class Singleton {
    private static Singleton m_instance = new Singleton();
    //构造方法私有，保证外界无法直接实例化
    private Singleton(){
    }
    //通过该方法获得实例对象
    public static Singleton getInstance() {
        return m_instance;
    }
}