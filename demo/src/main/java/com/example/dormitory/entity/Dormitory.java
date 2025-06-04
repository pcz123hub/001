package com.example.dormitory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dormitory")
public class Dormitory {

    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String building;
    private String roomNumber;
    private Integer capacity;
    private Integer currentNumber;
    private String status; // 正常/维修中
}    