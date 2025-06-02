package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.RoomAssignmentDto;
import com.itheima.reggie.entity.RoomAssignment;
import com.itheima.reggie.entity.UserAccount;
import com.itheima.reggie.service.RoomAssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/assignment")
public class RoomAssignmentController {

    @Autowired
    private RoomAssignmentService roomAssignmentService;

    private boolean hasStaffOrAdminRole(HttpServletRequest request) {
        UserAccount user = (UserAccount) request.getSession().getAttribute("user");
        return user != null && ("ROLE_ADMIN".equals(user.getRole()) || "ROLE_STAFF".equals(user.getRole()));
    }

    @PostMapping
    public R<String> assign(HttpServletRequest request, @RequestBody RoomAssignmentDto assignmentDto) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Assigning student to room: {}", assignmentDto);
        roomAssignmentService.assignStudentToRoom(assignmentDto);
        return R.success("Student assigned successfully.");
    }

    @PutMapping("/unassign/{id}")
    public R<String> unassign(HttpServletRequest request, @PathVariable Long id, 
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualLeaveDate,
                              @RequestParam(required=false) String notes) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Unassigning student from assignment id: {}", id);
        roomAssignmentService.unassignStudentFromRoom(id, actualLeaveDate, notes);
        return R.success("Student unassigned successfully.");
    }

    @GetMapping("/{id}")
    public R<RoomAssignmentDto> getAssignmentDetails(HttpServletRequest request, @PathVariable Long id) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        RoomAssignmentDto dto = roomAssignmentService.getAssignmentByIdWithDetails(id);
        return dto != null ? R.success(dto) : R.error("Assignment not found.");
    }

    @GetMapping("/page")
    public R<Page<RoomAssignmentDto>> pageDetails(HttpServletRequest request, int page, int pageSize,
                                                  @RequestParam(required = false) Long studentId,
                                                  @RequestParam(required = false) Long roomId,
                                                  @RequestParam(required = false) Boolean isActive) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        Page<RoomAssignmentDto> dtoPage = roomAssignmentService.getPageOfAssignmentsWithDetails(page, pageSize, studentId, roomId, isActive);
        return R.success(dtoPage);
    }
    
    @GetMapping("/student/{studentId}/active")
    public R<RoomAssignment> getActiveAssignmentForStudent(HttpServletRequest request, @PathVariable Long studentId) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        RoomAssignment assignment = roomAssignmentService.getActiveAssignmentByStudentId(studentId);
        return R.success(assignment); 
    }
}
