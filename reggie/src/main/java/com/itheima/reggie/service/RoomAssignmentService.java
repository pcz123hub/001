package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.RoomAssignmentDto;
import com.itheima.reggie.entity.RoomAssignment;
import java.time.LocalDate;

public interface RoomAssignmentService extends IService<RoomAssignment> {
    boolean assignStudentToRoom(RoomAssignmentDto assignmentDto);
    boolean unassignStudentFromRoom(Long assignmentId, LocalDate actualLeaveDate, String notes);
    RoomAssignmentDto getAssignmentByIdWithDetails(Long id);
    Page<RoomAssignmentDto> getPageOfAssignmentsWithDetails(int pageNum, int pageSize, Long studentId, Long roomId, Boolean isActive);
    RoomAssignment getActiveAssignmentByStudentId(Long studentId);
    int countActiveAssignmentsByRoomId(Long roomId);
}
