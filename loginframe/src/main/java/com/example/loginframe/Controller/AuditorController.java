package com.example.loginframe.Controller;

import com.example.loginframe.Entity.AuditUpdate;
import com.example.loginframe.Service.AuditorService;
import com.example.loginframe.dto.AuditRemarkRequest;
import com.example.loginframe.dto.AuditStatusUpdateRequest;
import com.example.loginframe.dto.AuditorAuditDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auditor")
public class AuditorController {

    private final AuditorService auditorService;

    @GetMapping("/my-audits")
    public List<AuditorAuditDTO> getAssignedAudits(@RequestParam String auditorEmail) {
        return auditorService.getAssignedAudits(auditorEmail);
    }

    @GetMapping("/audit/{auditId}")
    public AuditorAuditDTO getAuditDetails(
            @PathVariable Long auditId,
            @RequestParam String auditorEmail
    ) {
        return auditorService.getAuditDetails(auditId, auditorEmail);
    }

    @GetMapping("/audit/{auditId}/updates")
    public List<AuditUpdate> getAuditUpdates(
            @PathVariable Long auditId,
            @RequestParam String auditorEmail
    ) {
        return auditorService.getAuditUpdates(auditId, auditorEmail);
    }

    @PostMapping("/audit/{auditId}/remark")
    public String addRemark(
            @PathVariable Long auditId,
            @RequestBody AuditRemarkRequest request
    ) {
        return auditorService.addRemark(auditId, request);
    }

    @PutMapping("/audit/{auditId}/status")
    public String updateStatus(
            @PathVariable Long auditId,
            @RequestBody AuditStatusUpdateRequest request
    ) {
        return auditorService.updateAuditStatus(auditId, request);
    }
}
