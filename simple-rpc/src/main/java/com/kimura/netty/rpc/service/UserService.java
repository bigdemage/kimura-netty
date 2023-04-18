package com.kimura.netty.rpc.service;

import com.kimura.netty.rpc.pojo.User;

import java.util.List;

public interface UserService {

    public User getByName(String name);

    public List<User> getByAge(Integer age);
}
