package com.example.demo0424.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dormitory")
public class Dormitory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;

    private Integer totalRooms;
}
