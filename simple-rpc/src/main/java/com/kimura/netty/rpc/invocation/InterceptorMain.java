package com.kimura.netty.rpc.invocation;

import com.kimura.netty.rpc.service.UserService;
import com.kimura.netty.rpc.service.impl.UserServiceImpl;
import net.sf.cglib.proxy.Enhancer;

public class InterceptorMain {
    public static void main(String[] args) {

        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class);
        //增强的interceptor
        enhancer.setCallback(new LogInterceptor());

        UserService service= (UserService) enhancer.create();
        service.getByName("kimura");

    }
}
