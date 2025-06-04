package com.example.dormitory.controller;

import com.example.dormitory.entity.Dormitory;
import com.example.dormitory.entity.Student;
import com.example.dormitory.service.DormitoryService;
import com.example.dormitory.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dormitory")
public class DormitoryController {

    @Autowired
    private DormitoryService dormitoryService;
    
    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Dormitory> dormitories = dormitoryService.list();
        model.addAttribute("dormitories", dormitories);
        return "dormitory/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("dormitory", new Dormitory());
        return "dormitory/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Dormitory dormitory) {
        dormitoryService.save(dormitory);
        return "redirect:/dormitory/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Dormitory dormitory = dormitoryService.getById(id);
        model.addAttribute("dormitory", dormitory);
        return "dormitory/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, @ModelAttribute Dormitory dormitory) {
        dormitory.setId(id);
        dormitoryService.updateById(dormitory);
        return "redirect:/dormitory/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        // 检查宿舍是否有学生
        List<Student> students = studentService.findByDormitoryId(id);
        if (students != null && !students.isEmpty()) {
            // 可以添加错误提示
            return "redirect:/dormitory/list";
        }
        
        dormitoryService.removeById(id);
        return "redirect:/dormitory/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Dormitory dormitory = dormitoryService.getById(id);
        List<Student> students = studentService.findByDormitoryId(id);
        
        model.addAttribute("dormitory", dormitory);
        model.addAttribute("students", students);
        return "dormitory/view";
    }
}    