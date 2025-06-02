package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // To include fields from Room
public class RoomDto extends Room {
    private String buildingName;
}
