package com.kimura.netty.rpc.invocation;

import com.kimura.netty.rpc.service.UserService;
import com.kimura.netty.rpc.service.impl.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class InvocationMain {

    public static void main(String[] args) {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        //获取类加载器
        ClassLoader classLoader = userServiceImpl.getClass().getClassLoader();
        //获取接口class
        Class[] interfaces = userServiceImpl.getClass().getInterfaces();
        //创建代理对象处理器
        InvocationHandler logHandler = new LogInvocationHandler<UserService>(userServiceImpl);
        UserService userService = (UserService) Proxy.newProxyInstance(classLoader, interfaces, logHandler);
        userService.getByAge(30);
//        System.out.println(String.format("结果:%s", JSONObject.toJSONString(user)));
        ProxyUtil.generateClassFile(userServiceImpl.getClass(),"UserServiceProxy");
    }
}
