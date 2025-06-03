package com.example.demo0424.controller;

import com.example.demo0424.entity.Student;
import com.example.demo0424.exception.GenderMismatchException;
import com.example.demo0424.exception.RoomFullException;
import com.example.demo0424.service.RoomService;
import com.example.demo0424.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final RoomService roomService;

    public StudentController(StudentService studentService, RoomService roomService) {
        this.studentService = studentService;
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.list());
        return "student/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("rooms", roomService.list());
        return "student/form";
    }

    @PostMapping("/save")
    @ResponseBody // Ensure this is present if not using ResponseEntity for all paths or if class not @RestController
    public ResponseEntity<?> saveStudent(@ModelAttribute Student student, Model model) {
        try {
            studentService.saveOrUpdate(student);
            return ResponseEntity.ok().body(Map.of("status", "success", "message", "Student saved successfully!"));
        } catch (RoomFullException | GenderMismatchException | IllegalArgumentException e) {
            logger.warn("Business exception while saving student: {}", e.getMessage());
            // For Spring validation errors, you'd typically return the model with errors back to the form view.
            // Here, for AJAX, we send a specific error message.
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error while saving student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "An unexpected error occurred saving the student."));
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getById(id));
        model.addAttribute("rooms", roomService.list()); // Assuming you want to list all rooms
        return "student/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.removeById(id);
        return "redirect:/students/";
    }
}
