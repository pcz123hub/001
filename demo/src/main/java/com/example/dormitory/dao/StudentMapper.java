package com.example.dormitory.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dormitory.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}    