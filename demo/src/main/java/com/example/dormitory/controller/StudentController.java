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
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private DormitoryService dormitoryService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Student> students = studentService.list();
        model.addAttribute("students", students);
        return "student/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("student", new Student());
        
        // 获取可用宿舍列表
        List<Dormitory> availableDormitories = dormitoryService.findAvailableDormitories();
        model.addAttribute("availableDormitories", availableDormitories);
        
        return "student/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Student student) {
        studentService.save(student);
        
        // 如果分配了宿舍
        if (student.getDormitoryId() != null) {
            studentService.assignDormitory(student.getId(), student.getDormitoryId());
        }
        
        return "redirect:/student/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        
        // 获取可用宿舍列表
        List<Dormitory> availableDormitories = dormitoryService.findAvailableDormitories();
        model.addAttribute("availableDormitories", availableDormitories);
        
        return "student/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, @ModelAttribute Student student) {
        student.setId(id);
        studentService.updateById(student);
        
        // 如果分配了宿舍
        if (student.getDormitoryId() != null) {
            studentService.assignDormitory(student.getId(), student.getDormitoryId());
        }
        
        return "redirect:/student/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        // 获取学生原宿舍信息
        Student student = studentService.getById(id);
        if (student != null && student.getDormitoryId() != null) {
            // 减少宿舍人数
            dormitoryService.removeStudent(student.getDormitoryId());
        }
        
        studentService.removeById(id);
        return "redirect:/student/list";
    }
}    