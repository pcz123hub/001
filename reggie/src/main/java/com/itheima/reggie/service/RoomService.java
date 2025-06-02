package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.RoomDto; // Will create this DTO
import com.itheima.reggie.entity.Room;

public interface RoomService extends IService<Room> {
   // Method to get paginated list of rooms with building names
   public Page<RoomDto> pageWithBuildingName(int page, int pageSize, String roomNumber, Long buildingId);
   
   // Method to get a single room with building name
   public RoomDto getByIdWithBuildingName(Long id);

   // Method to save room (could be overridden if special logic needed)
   public boolean saveRoom(RoomDto roomDto);

   // Method to update room (could be overridden if special logic needed)
   public boolean updateRoomById(RoomDto roomDto);
}
