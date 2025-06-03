package com.example.demo0424.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo0424.dao.RoomMapper;
import com.example.demo0424.entity.Room;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
}
