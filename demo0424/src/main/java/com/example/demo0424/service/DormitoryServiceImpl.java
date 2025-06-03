package com.example.demo0424.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo0424.dao.DormitoryMapper;
import com.example.demo0424.entity.Dormitory;
import org.springframework.stereotype.Service;

@Service
public class DormitoryServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory> implements DormitoryService {
}
