package com.example.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dormitory.entity.User;

public interface UserService extends IService<User> {

    boolean register(User user);
    
    User getByUsername(String username);
}    