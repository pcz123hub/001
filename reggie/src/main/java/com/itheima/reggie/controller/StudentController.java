package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.RoomAssignment;
import com.itheima.reggie.entity.Student;
import com.itheima.reggie.entity.UserAccount;
import com.itheima.reggie.service.RoomAssignmentService;
import com.itheima.reggie.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private RoomAssignmentService roomAssignmentService;

    private boolean hasStaffOrAdminRole(HttpServletRequest request) {
        UserAccount user = (UserAccount) request.getSession().getAttribute("user");
        return user != null && ("ROLE_ADMIN".equals(user.getRole()) || "ROLE_STAFF".equals(user.getRole()));
    }

    // Add (Create)
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Student student) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Adding new student: {}", student.getName());
        // Basic validation example: Ensure studentIdNumber and email are unique if possible
        // This can be handled by database constraints + GlobalExceptionHandler
        try {
            if (studentService.save(student)) {
                return R.success("Student added successfully");
            }
            return R.error("Failed to add student");
        } catch (Exception e) {
            // Catch potential exceptions from unique constraints
            log.error("Error saving student: " + e.getMessage());
            if (e.getMessage().toLowerCase().contains("duplicate entry")) {
                if (e.getMessage().contains(student.getStudentIdNumber())) {
                   return R.error("Student ID Number already exists.");
                }
                if (e.getMessage().contains(student.getEmail())) {
                   return R.error("Student Email already exists.");
                }
            }
            return R.error("Failed to add student due to an error.");
        }
    }

    // Get by ID (Read)
    @GetMapping("/{id}")
    public R<Student> get(HttpServletRequest request, @PathVariable Long id) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Getting student with id: {}", id);
        Student student = studentService.getById(id);
        if (student != null) {
            return R.success(student);
        }
        return R.error("Student not found");
    }

    // Paginated list (Read)
    @GetMapping("/page")
    public R<Page<Student>> page(HttpServletRequest request, int page, int pageSize, String name, String studentIdNumber) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Fetching page of students: page={}, pageSize={}, name={}, studentIdNumber={}", page, pageSize, name, studentIdNumber);
        Page<Student> studentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Student::getName, name);
        queryWrapper.like(StringUtils.isNotEmpty(studentIdNumber), Student::getStudentIdNumber, studentIdNumber);
        queryWrapper.orderByDesc(Student::getUpdateTime); // Or by name, etc.
        studentService.page(studentPage, queryWrapper);
        return R.success(studentPage);
    }

    // Update
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Student student) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Updating student: {}", student.getName());
        try {
           if (studentService.updateById(student)) {
               return R.success("Student updated successfully");
           }
           return R.error("Failed to update student");
        } catch (Exception e) {
            log.error("Error updating student: " + e.getMessage());
             if (e.getMessage().toLowerCase().contains("duplicate entry")) {
                // Check which field caused the duplicate error if possible (might be complex)
                return R.error("Update failed: Student ID Number or Email might already exist for another student.");
            }
            return R.error("Failed to update student due to an error.");
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public R<String> delete(HttpServletRequest request, @PathVariable Long id) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Deleting student with id: {}", id);
        // TODO: Add check: Do not delete if student is assigned to a room.
        // This will require checking the Assignment/Lease table later.
        // For now, direct delete.
        RoomAssignment activeAssignment = roomAssignmentService.getActiveAssignmentByStudentId(id);
        if (activeAssignment != null) {
            return R.error("Cannot delete student: Student has an active room assignment. Please unassign first.");
        }
        if (studentService.removeById(id)) {
            return R.success("Student deleted successfully");
        }
        return R.error("Failed to delete student");
    }
}
