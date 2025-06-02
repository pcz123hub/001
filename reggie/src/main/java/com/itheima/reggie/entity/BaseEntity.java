package com.itheima.reggie.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaseEntity {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}
