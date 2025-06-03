package com.example.demo0424.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("room")
public class Room {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String roomNumber;

    private Long dormitoryId;

    private Integer capacity;

    private Integer currentOccupancy = 0;

    private String genderType; // MALE, FEMALE, ANY
}
