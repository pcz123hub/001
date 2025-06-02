package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R; // Assuming R class exists for standardized responses
import com.itheima.reggie.entity.Building;
import com.itheima.reggie.service.BuildingService;
import com.itheima.reggie.entity.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

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
    public R<String> save(HttpServletRequest request, @RequestBody Building building) {
        if (!hasAdminRole(request)) {
            return R.error("Access Denied: Admin role required.");
        }
        log.info("Adding new building: {}", building);
        if (buildingService.save(building)) {
            return R.success("Building added successfully");
        }
        return R.error("Failed to add building");
    }

    // Get by ID (Read)
    @GetMapping("/{id}")
    public R<Building> get(HttpServletRequest request, @PathVariable Long id) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Getting building with id: {}", id);
        Building building = buildingService.getById(id);
        if (building != null) {
            return R.success(building);
        }
        return R.error("Building not found");
    }

    // List all (Read) - Basic, no pagination yet
    @GetMapping("/list")
    public R<List<Building>> list(HttpServletRequest request) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("Fetching all buildings");
        List<Building> list = buildingService.list();
        return R.success(list);
    }
    
    // Page query (Read)
    @GetMapping("/page")
    public R<Page> page(HttpServletRequest request, int page, int pageSize, String name) {
        if (!hasStaffOrAdminRole(request)) {
            return R.error("Access Denied: Staff or Admin role required.");
        }
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        Page<Building> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Building> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Building::getName, name);
        queryWrapper.orderByDescending(Building::getUpdateTime); // Or by name, etc.
        buildingService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    // Update
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Building building) {
        if (!hasAdminRole(request)) {
            return R.error("Access Denied: Admin role required.");
        }
        log.info("Updating building: {}", building);
        if (buildingService.updateById(building)) {
            return R.success("Building updated successfully");
        }
        return R.error("Failed to update building");
    }

    // Delete
    @DeleteMapping("/{id}")
    public R<String> delete(HttpServletRequest request, @PathVariable Long id) {
        if (!hasAdminRole(request)) {
            return R.error("Access Denied: Admin role required.");
        }
        log.info("Deleting building with id: {}", id);
        if (buildingService.removeById(id)) {
            return R.success("Building deleted successfully");
        }
        return R.error("Failed to delete building");
    }
}
