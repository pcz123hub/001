package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RoomAssignment implements Serializable {
    private static final long serialVersionUID = 1L; // Corrected

    private Long id;
    private Long studentId;
    private Long roomId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedLeaveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualLeaveDate;

    private String notes;
    private Boolean isActive;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
