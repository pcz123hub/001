package com.example.demo0424.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("student")
public class Book {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String studentid ;
    private String name ;
    private String classname ;
    private Double score ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString(){
        return "Employee{" +
                "id=" + id +
                ", studentid='" + studentid +
                ", name='" + name +
                ", classname='" + classname +
                ", score='" + score +
                '}';
    }
}
