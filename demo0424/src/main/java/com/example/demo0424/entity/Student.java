package com.example.demo0424.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("student")
public class Student {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String studentId;

    private String firstName;

    private String lastName;

    private String gender; // MALE, FEMALE

    private String email;

    private String phoneNumber;

    private Long roomId;

    private LocalDate dateOfBirth;
}
