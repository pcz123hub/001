package com.example.demo0424.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo0424.entity.UserDto;

public interface UserService extends IService<UserDto> {
    boolean insert(UserDto userDto);
}
