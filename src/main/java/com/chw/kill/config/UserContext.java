package com.chw.kill.config;

import com.chw.kill.domain.User;

/**
 * @Author Chihw
 * @Description     让每个线程绑定自己的值，高并发多线程场景下。
 * 如果在公共线程里面存用户信息可能会造成用户信息的紊乱，所以当前登录的用户信息都存在自己的线程里
 * 避免线程安全问题
 * @Date 2021/6/19 9:59
 */
public class UserContext {

    private static ThreadLocal<User> userHolder=new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();

    }
}
