package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.RoomDto;
import com.itheima.reggie.entity.Room; // Keep for simple list if needed
import com.itheima.reggie.entity.UserAccount;
import com.itheima.reggie.service.RoomAssignmentService;
import com.itheima.reggie.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List; // Keep for simple list if needed

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomAssignmentService roomAssignmentService;

    private boolean hasAdminRole(HttpServletRequest request) {
        UserAccount user = (UserAccount) request.getSession().getAttribute("user");
        return user != null && "ROLE_ADMIN".equals(user.getRole());
    }

    private boolean hasStaffOrAdminRole(HttpServletRequest request) {
        UserAccount user = (UserAccount) request.getSession().getAttribute("user");
        return user != null && ("ROLE_ADMIN".equals(user.getRole()) || "ROLE_STAFF".equals(user.getRole()));
    }

    // Add (Create)
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody RoomDto roomDto) {
        if (!hasAdminRole(request)) {
            return R.error("Access Denied: Admin role required.");
        }
        log.info("Adding new room: {}", roomDto);
        if (roomService.saveRoom(roomDto)) {
            return R.success("Room added successfully");
        }
        return R.error("Failed to add room");
    }

    // Get by ID (Read) - with Building Name
    @GetMapping("/{id}")
    public R<RoomDto> get(HttpServletRequest request, @PathVariable Long id) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Getting room with id: {}", id);
        RoomDto roomDto = roomService.getByIdWithBuildingName(id);
        if (roomDto != null) {
            return R.success(roomDto);
        }
        return R.error("Room not found");
    }

    // Paginated list with building names (Read)
    @GetMapping("/page")
    public R<Page<RoomDto>> page(HttpServletRequest request, int page, int pageSize, String roomNumber, Long buildingId) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Fetching page of rooms: page={}, pageSize={}, roomNumber={}, buildingId={}", page, pageSize, roomNumber, buildingId);
        Page<RoomDto> roomDtoPage = roomService.pageWithBuildingName(page, pageSize, roomNumber, buildingId);
        return R.success(roomDtoPage);
    }
    
    // Optional: Simple list of all rooms (without building name, or could be adapted)
    @GetMapping("/list")
    public R<List<Room>> listAllRooms(HttpServletRequest request, @RequestParam(required = false) Long buildingId) {
       if (!hasStaffOrAdminRole(request)) {
           return R.error("Access Denied: Staff or Admin role required.");
       }
       log.info("Fetching all rooms, optional buildingId: {}", buildingId);
       // This is a basic list. For UI, /page is likely preferred.
       // You might want a simpler list for dropdowns, perhaps just rooms of a specific building.
       List<Room> list = roomService.lambdaQuery()
                           .eq(buildingId != null, Room::getBuildingId, buildingId)
                           .list();
       return R.success(list);
    }


    // Update
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody RoomDto roomDto) {
        if (!hasAdminRole(request)) {
            return R.error("Access Denied: Admin role required.");
        }
        log.info("Updating room: {}", roomDto);
        if (roomService.updateRoomById(roomDto)) {
            return R.success("Room updated successfully");
        }
        return R.error("Failed to update room");
    }

    // Delete
    @DeleteMapping("/{id}")
    public R<String> delete(HttpServletRequest request, @PathVariable Long id) {
        if (!hasAdminRole(request)) {
            return R.error("Access Denied: Admin role required.");
        }
        log.info("Deleting room with id: {}", id);
        // Add check: Do not delete if current_occupancy > 0
        Room room = roomService.getById(id);
        if (room != null && room.getCurrentOccupancy() != null && room.getCurrentOccupancy() > 0) {
            return R.error("Cannot delete room with current occupants.");
        }
        if (roomAssignmentService.countActiveAssignmentsByRoomId(id) > 0) {
            return R.error("Cannot delete room: It has active assignments.");
        }
        if (roomService.removeById(id)) {
            return R.success("Room deleted successfully");
        }
        return R.error("Failed to delete room");
    }
}
