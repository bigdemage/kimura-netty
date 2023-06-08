package com.kimura.netty.tomcat.service;

import com.kimura.netty.tomcat.pojo.User;

import java.util.List;

public interface UserService {

    public User getByName(String name);

    public List<User> getByAge(Integer age);
}
