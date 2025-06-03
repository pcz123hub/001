package com.example.demo0424.entity;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("student")
public class Privilege {
    private Integer id;
    private String authority;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
