package com.example.demo0424.controller;

import com.example.demo0424.entity.Room;
import com.example.demo0424.service.DormitoryService;
import com.example.demo0424.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final DormitoryService dormitoryService;

    public RoomController(RoomService roomService, DormitoryService dormitoryService) {
        this.roomService = roomService;
        this.dormitoryService = dormitoryService;
    }

    @GetMapping("/")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.list());
        return "room/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("dormitories", dormitoryService.list());
        return "room/form";
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute Room room) {
        roomService.saveOrUpdate(room);
        return "redirect:/rooms/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.getById(id));
        model.addAttribute("dormitories", dormitoryService.list());
        return "room/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.removeById(id);
        return "redirect:/rooms/";
    }
}
