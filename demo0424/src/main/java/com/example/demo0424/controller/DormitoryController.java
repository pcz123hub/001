package com.example.demo0424.controller;

import com.example.demo0424.entity.Dormitory;
import com.example.demo0424.service.DormitoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dormitories")
public class DormitoryController {

    private final DormitoryService dormitoryService;

    public DormitoryController(DormitoryService dormitoryService) {
        this.dormitoryService = dormitoryService;
    }

    @GetMapping("/")
    public String listDormitories(Model model) {
        model.addAttribute("dormitories", dormitoryService.list());
        return "dormitory/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("dormitory", new Dormitory());
        return "dormitory/form";
    }

    @PostMapping("/save")
    public String saveDormitory(@ModelAttribute Dormitory dormitory) {
        dormitoryService.saveOrUpdate(dormitory); // Use saveOrUpdate for both create and update
        return "redirect:/dormitories/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("dormitory", dormitoryService.getById(id));
        return "dormitory/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteDormitory(@PathVariable Long id) {
        dormitoryService.removeById(id);
        return "redirect:/dormitories/";
    }
}
