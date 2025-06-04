package com.example.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dormitory.dao.StudentMapper;
import com.example.dormitory.entity.Student;
import com.example.dormitory.service.DormitoryService;
import com.example.dormitory.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private DormitoryService dormitoryService;

    @Override
    public List<Student> findByDormitoryId(Integer dormitoryId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dormitory_id", dormitoryId);
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public boolean assignDormitory(Integer studentId, Integer dormitoryId) {
        // 检查宿舍是否存在且可分配
        if (!dormitoryService.allocateStudent(dormitoryId)) {
            return false;
        }
        
        // 更新学生宿舍信息
        Student student = getById(studentId);
        if (student == null) {
            // 分配失败，回滚宿舍分配
            dormitoryService.removeStudent(dormitoryId);
            return false;
        }
        
        // 如果学生之前已分配宿舍，减少原宿舍人数
        if (student.getDormitoryId() != null && !student.getDormitoryId().equals(dormitoryId)) {
            dormitoryService.removeStudent(student.getDormitoryId());
        }
        
        student.setDormitoryId(dormitoryId);
        return updateById(student);
    }
}    