package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.RoomDto;
import com.itheima.reggie.entity.Building;
import com.itheima.reggie.entity.Room;
import com.itheima.reggie.mapper.RoomMapper;
import com.itheima.reggie.service.BuildingService;
import com.itheima.reggie.service.RoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Autowired
    private BuildingService buildingService;

    @Override
    @Transactional
    public Page<RoomDto> pageWithBuildingName(int pageNum, int pageSize, String roomNumber, Long buildingId) {
        Page<Room> roomPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(roomNumber), Room::getRoomNumber, roomNumber);
        queryWrapper.eq(buildingId != null, Room::getBuildingId, buildingId);
        queryWrapper.orderByAsc(Room::getBuildingId).orderByAsc(Room::getRoomNumber); // Example ordering
        this.page(roomPage, queryWrapper);

        Page<RoomDto> roomDtoPage = new Page<>();
        BeanUtils.copyProperties(roomPage, roomDtoPage, "records");

        List<RoomDto> roomDtoList = roomPage.getRecords().stream().map(room -> {
            RoomDto roomDto = new RoomDto();
            BeanUtils.copyProperties(room, roomDto);
            Building building = buildingService.getById(room.getBuildingId());
            if (building != null) {
                roomDto.setBuildingName(building.getName());
            }
            return roomDto;
        }).collect(Collectors.toList());

        roomDtoPage.setRecords(roomDtoList);
        return roomDtoPage;
    }
    
    @Override
    public RoomDto getByIdWithBuildingName(Long id) {
       Room room = this.getById(id);
       if (room == null) {
           return null;
       }
       RoomDto roomDto = new RoomDto();
       BeanUtils.copyProperties(room, roomDto);
       Building building = buildingService.getById(room.getBuildingId());
       if (building != null) {
           roomDto.setBuildingName(building.getName());
       }
       return roomDto;
    }

   @Override
   public boolean saveRoom(RoomDto roomDto) {
       // Can add validation here, e.g., check if buildingId is valid
       Room room = new Room();
       BeanUtils.copyProperties(roomDto, room); // buildingName will be ignored as it's not in Room entity
       return this.save(room);
   }

   @Override
   public boolean updateRoomById(RoomDto roomDto) {
       // Can add validation here
       Room room = new Room();
       BeanUtils.copyProperties(roomDto, room);
       return this.updateById(room);
   }
}
