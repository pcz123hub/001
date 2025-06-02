package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException; 
import com.itheima.reggie.dto.RoomAssignmentDto;
import com.itheima.reggie.dto.RoomDto; 
import com.itheima.reggie.entity.Room;
import com.itheima.reggie.entity.RoomAssignment;
import com.itheima.reggie.entity.Student;
import com.itheima.reggie.mapper.RoomAssignmentMapper;
import com.itheima.reggie.service.RoomService;
import com.itheima.reggie.service.StudentService;
import com.itheima.reggie.service.RoomAssignmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomAssignmentServiceImpl extends ServiceImpl<RoomAssignmentMapper, RoomAssignment> implements RoomAssignmentService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private StudentService studentService;
    

    @Override
    @Transactional
    public boolean assignStudentToRoom(RoomAssignmentDto assignmentDto) {
        Student student = studentService.getById(assignmentDto.getStudentId());
        if (student == null) {
            throw new CustomException("Student not found.");
        }
        Room room = roomService.getById(assignmentDto.getRoomId());
        if (room == null) {
            throw new CustomException("Room not found.");
        }

        RoomAssignment activeAssignment = getActiveAssignmentByStudentId(assignmentDto.getStudentId());
        if (activeAssignment != null) {
            throw new CustomException("Student already has an active assignment.");
        }

        if (room.getCurrentOccupancy() == null) room.setCurrentOccupancy(0);
        if (room.getCurrentOccupancy() >= room.getCapacity()) {
            throw new CustomException("Room is already full.");
        }

        RoomAssignment assignment = new RoomAssignment();
        BeanUtils.copyProperties(assignmentDto, assignment);
        assignment.setIsActive(true);
        if (assignment.getAssignmentDate() == null) {
            assignment.setAssignmentDate(LocalDate.now());
        }

        this.save(assignment);
        room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
        roomService.updateById(room);
        return true;
    }

    @Override
    @Transactional
    public boolean unassignStudentFromRoom(Long assignmentId, LocalDate actualLeaveDate, String notes) {
        RoomAssignment assignment = this.getById(assignmentId);
        if (assignment == null || !assignment.getIsActive()) {
            throw new CustomException("Active assignment not found or already inactive.");
        }

        Room room = roomService.getById(assignment.getRoomId());
        if (room == null) {
            throw new CustomException("Associated room not found. Data inconsistency.");
        }

        assignment.setIsActive(false);
        assignment.setActualLeaveDate(actualLeaveDate != null ? actualLeaveDate : LocalDate.now());
        if(notes != null && !notes.isEmpty()) {
           String currentNotes = assignment.getNotes() == null ? "" : assignment.getNotes() + " ";
           assignment.setNotes(currentNotes + "Unassigned: " + notes);
        }


        this.updateById(assignment);

        if (room.getCurrentOccupancy() != null && room.getCurrentOccupancy() > 0) {
            room.setCurrentOccupancy(room.getCurrentOccupancy() - 1);
            roomService.updateById(room);
        }
        return true;
    }
    
    @Override
    public RoomAssignmentDto getAssignmentByIdWithDetails(Long id) {
        RoomAssignment assignment = this.getById(id);
        if (assignment == null) return null;
        return convertToDto(assignment);
    }
    
    @Override
    public Page<RoomAssignmentDto> getPageOfAssignmentsWithDetails(int pageNum, int pageSize, Long studentId, Long roomId, Boolean isActive) {
        Page<RoomAssignment> assignmentPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RoomAssignment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(studentId != null, RoomAssignment::getStudentId, studentId);
        queryWrapper.eq(roomId != null, RoomAssignment::getRoomId, roomId);
        queryWrapper.eq(isActive != null, RoomAssignment::getIsActive, isActive);
        queryWrapper.orderByDesc(RoomAssignment::getAssignmentDate); 
        this.page(assignmentPage, queryWrapper);

        Page<RoomAssignmentDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(assignmentPage, dtoPage, "records");
        List<RoomAssignmentDto> dtoList = assignmentPage.getRecords().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        return dtoPage;
    }

    private RoomAssignmentDto convertToDto(RoomAssignment assignment) {
        RoomAssignmentDto dto = new RoomAssignmentDto();
        BeanUtils.copyProperties(assignment, dto);
        Student student = studentService.getById(assignment.getStudentId());
        if (student != null) {
            dto.setStudentName(student.getName());
            dto.setStudentIdNumber(student.getStudentIdNumber());
        }
        RoomDto roomDto = roomService.getByIdWithBuildingName(assignment.getRoomId()); 
        if (roomDto != null) {
            dto.setRoomNumber(roomDto.getRoomNumber());
            dto.setBuildingName(roomDto.getBuildingName()); 
        }
        return dto;
    }

    @Override
    public RoomAssignment getActiveAssignmentByStudentId(Long studentId) {
        LambdaQueryWrapper<RoomAssignment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoomAssignment::getStudentId, studentId);
        queryWrapper.eq(RoomAssignment::getIsActive, true);
        return this.getOne(queryWrapper);
    }
    
    @Override
    public int countActiveAssignmentsByRoomId(Long roomId) {
       LambdaQueryWrapper<RoomAssignment> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.eq(RoomAssignment::getRoomId, roomId);
       queryWrapper.eq(RoomAssignment::getIsActive, true);
       return this.count(queryWrapper);
    }
}
