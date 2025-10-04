package com.simple.pmtool.Controller;

import com.simple.pmtool.DTO.ChangeLogRequest;
import com.simple.pmtool.Model.ChangeLog;
import com.simple.pmtool.Service.ChangeLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/test04")
//@CrossOrigin(origins = {"http://localhost:3000", "https://steamiest-ellamae-lustrelessly.ngrok-free.dev", "http://localhost:3001"})
public class ChangeLogController {

    private final ChangeLogService changeLogService;

    public ChangeLogController(ChangeLogService changeLogService) {
        this.changeLogService = changeLogService;
    }

    // Anyone with a valid JWT (USER, ADMIN, SUPER_ADMIN) can view logs
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/get_all_logs")
    public ResponseEntity<List<ChangeLog>> getAllLogs() {
        return ResponseEntity.ok(changeLogService.getAllLogs());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/get_logs_by_task")
    public ResponseEntity<List<ChangeLog>> getLogsByTask(@RequestParam Long taskId) {
        return ResponseEntity.ok(changeLogService.getLogsByTask(taskId));
    }

    // Create changelog (USER, ADMIN, SUPER_ADMIN)
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @PostMapping("/create_changelog")
    public ResponseEntity<ChangeLog> createLog(@RequestBody ChangeLogRequest request) {
        String jwtUserId = null;

        // Get userId from SecurityContext
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            jwtUserId = principal != null ? principal.toString() : null;
        }

        if (jwtUserId == null) {
            return ResponseEntity.status(403).build(); // explicit 403 if not authenticated
        }

        ChangeLog log = new ChangeLog();
        log.setTaskId(request.getTaskId());
        log.setOldStatus(request.getOldStatus());
        log.setNewStatus(request.getNewStatus());
        log.setRemark(request.getRemark());
        log.setAction(request.getAction() != null ? request.getAction() : "STATUS_CHANGED");
        log.setUserId(jwtUserId);

        return ResponseEntity.ok(changeLogService.createLog(log));
    }

    // Delete changelog (SUPER_ADMIN only)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/delete_changelog")
    public ResponseEntity<String> deleteLog(@RequestParam Long id) {
        try {
            changeLogService.deleteLog(id);
            return ResponseEntity.ok("Changelog deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete changelog.");
        }
    }
}
