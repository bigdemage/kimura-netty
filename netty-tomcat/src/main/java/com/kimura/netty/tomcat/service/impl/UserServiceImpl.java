package com.kimura.netty.tomcat.service.impl;

import com.kimura.netty.tomcat.pojo.User;
import com.kimura.netty.tomcat.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {


    public static final ConcurrentMap<String, User> users = new ConcurrentHashMap();

    static {
        users.put("kimura", User.builder().name("kimura").age(11).build());
        users.put("alibaba", User.builder().name("alibaba").age(20).build());
        users.put("fyx", User.builder().name("fyx").age(30).build());
        users.put("sg", User.builder().name("sg").age(30).build());
        users.put("lz", User.builder().name("lz").age(30).build());
    }

    @Override
    public User getByName(String name) {
        return users.get(name);
    }

    @Override
    public List<User> getByAge(Integer age) {
        List<User> result = users.keySet().stream().map(key ->
        {
            User user = users.get(key);
            if (user.getAge().equals(age)) {
                return user;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return result;
    }
}
