package com.example.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dormitory.entity.Dormitory;

import java.util.List;

public interface DormitoryService extends IService<Dormitory> {

    List<Dormitory> findAvailableDormitories();
    
    boolean allocateStudent(Integer dormitoryId);
    
    boolean removeStudent(Integer dormitoryId);
}    