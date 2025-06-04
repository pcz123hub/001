package com.example.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dormitory.entity.Student;

import java.util.List;

public interface StudentService extends IService<Student> {

    List<Student> findByDormitoryId(Integer dormitoryId);
    
    boolean assignDormitory(Integer studentId, Integer dormitoryId);
}    