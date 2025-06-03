package com.example.demo0424.controller;

import com.example.demo0424.entity.UserDto;
import com.example.demo0424.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/doRegister")
    public String doRegister(UserDto userDto){

        if (userService.insert(userDto)){
            return "redirect:/loginview";
        }else{
            return "register";
        }
    }
}
