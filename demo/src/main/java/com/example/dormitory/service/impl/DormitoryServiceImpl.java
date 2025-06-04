package com.example.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dormitory.dao.DormitoryMapper;
import com.example.dormitory.entity.Dormitory;
import com.example.dormitory.service.DormitoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DormitoryServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory> implements DormitoryService {

    @Override
    public List<Dormitory> findAvailableDormitories() {
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "正常")
                    .lt("current_number", "capacity");
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public boolean allocateStudent(Integer dormitoryId) {
        Dormitory dormitory = getById(dormitoryId);
        if (dormitory == null || !"正常".equals(dormitory.getStatus()) || dormitory.getCurrentNumber() >= dormitory.getCapacity()) {
            return false;
        }
        
        dormitory.setCurrentNumber(dormitory.getCurrentNumber() + 1);
        return updateById(dormitory);
    }

    @Override
    @Transactional
    public boolean removeStudent(Integer dormitoryId) {
        Dormitory dormitory = getById(dormitoryId);
        if (dormitory == null || dormitory.getCurrentNumber() <= 0) {
            return false;
        }
        
        dormitory.setCurrentNumber(dormitory.getCurrentNumber() - 1);
        return updateById(dormitory);
    }
}    