package com.example.dormitory.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dormitory.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}    