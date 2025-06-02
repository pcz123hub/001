package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Building implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer numberOfFloors;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser; // Will be null for now

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser; // Will be null for now
}
