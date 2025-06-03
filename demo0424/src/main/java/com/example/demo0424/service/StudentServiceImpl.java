package com.example.demo0424.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo0424.dao.StudentMapper;
import com.example.demo0424.entity.Room;
import com.example.demo0424.entity.Student;
import com.example.demo0424.exception.GenderMismatchException;
import com.example.demo0424.exception.RoomFullException;
import com.example.demo0424.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    private final RoomService roomService;

    @Autowired
    public StudentServiceImpl(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    @Transactional
    public boolean saveOrUpdate(Student student) {
        if (student == null) {
            return false;
        }

        Long studentId = student.getId();
        Long oldRoomId = null;

        // If it's an update, get the student's current state to find oldRoomId
        if (studentId != null) {
            Student existingStudent = getBaseMapper().selectById(studentId);
            if (existingStudent != null) {
                oldRoomId = existingStudent.getRoomId();
            }
        }

        Long newRoomId = student.getRoomId();

        // Check if room assignment has changed
        if (!Objects.equals(oldRoomId, newRoomId)) {
            // Handle old room (if student was previously assigned)
            if (oldRoomId != null) {
                Room oldRoom = roomService.getById(oldRoomId);
                if (oldRoom != null) {
                    oldRoom.setCurrentOccupancy(Math.max(0, oldRoom.getCurrentOccupancy() - 1));
                    roomService.updateById(oldRoom);
                }
            }

            // Handle new room (if student is being assigned to a new room)
            if (newRoomId != null) {
                Room newRoom = roomService.getById(newRoomId);
                if (newRoom == null) {
                    throw new IllegalArgumentException("Room with ID " + newRoomId + " not found.");
                }

                // Capacity Check
                if (newRoom.getCurrentOccupancy() >= newRoom.getCapacity()) {
                    throw new RoomFullException("Room " + newRoom.getRoomNumber() + " (ID: " + newRoomId + ") is already full.");
                }

                // Gender Check
                if (newRoom.getGenderType() != null && !newRoom.getGenderType().equalsIgnoreCase("ANY")) {
                    if (student.getGender() == null || !student.getGender().equalsIgnoreCase(newRoom.getGenderType())) {
                        throw new GenderMismatchException("Student gender (" + student.getGender() +
                                ") does not match room gender type (" + newRoom.getGenderType() +
                                ") for room " + newRoom.getRoomNumber() + " (ID: " + newRoomId + ").");
                    }
                }

                newRoom.setCurrentOccupancy(newRoom.getCurrentOccupancy() + 1);
                roomService.updateById(newRoom);
            }
        }
        // Proceed to save/update the student entity itself
        return super.saveOrUpdate(student);
    }

    @Override
    @Transactional
    public boolean removeById(Long id) {
        if (id == null) {
            return false;
        }
        Student student = getBaseMapper().selectById(id);
        if (student != null && student.getRoomId() != null) {
            Room room = roomService.getById(student.getRoomId());
            if (room != null) {
                room.setCurrentOccupancy(Math.max(0, room.getCurrentOccupancy() - 1));
                roomService.updateById(room);
            }
        }
        return super.removeById(id);
    }
}
