package com.itheima.reggie.dto;

import com.itheima.reggie.entity.RoomAssignment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoomAssignmentDto extends RoomAssignment {
    private String studentName;
    private String studentIdNumber;
    private String roomNumber;
    private String buildingName; 
}
