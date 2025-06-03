package com.example.demo0424.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo0424.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
