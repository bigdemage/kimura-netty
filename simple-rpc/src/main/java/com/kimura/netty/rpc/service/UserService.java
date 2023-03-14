package com.kimura.netty.rpc.service;

import com.kimura.netty.rpc.pojo.User;

public interface UserService {

    public User getByName(String name);

}
