package com.example.dormitory.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    
// 导入 GetMapping 注解所在的包
@GetMapping("/index")
    public String index() {
        return "index";  // 返回 index.html
    }

}
