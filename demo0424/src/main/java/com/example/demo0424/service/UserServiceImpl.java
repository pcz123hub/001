package com.example.demo0424.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo0424.dao.UserMapper;
import com.example.demo0424.entity.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDto> implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Integer INSERT_SUCCESS = 1;

    public boolean insert(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userMapper.insert(userDto) == INSERT_SUCCESS;
    }

}
