package com.kimura.netty.rpc.service.impl;

import com.kimura.netty.rpc.service.User;
import com.kimura.netty.rpc.service.UserService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserServiceImpl implements UserService {


    public static final ConcurrentMap<String,User> users=new ConcurrentHashMap();

    static{
        users.put("kimura",User.builder().name("kimura").age(11).build());
        users.put("alibaba",User.builder().name("alibaba").age(20).build());
        users.put("fyx",User.builder().name("fyx").age(30).build());
    }

    @Override
    public User getByName(String name) {
        return users.get(name);
    }
}
