package com.example.loginframe.Controller;

import com.example.loginframe.Entity.AuditDetails;
import com.example.loginframe.Entity.AuditUpdate;
import com.example.loginframe.Service.AssiginAuditor;
import com.example.loginframe.Service.AuditDetailService;
import com.example.loginframe.Service.AuditorService;
import com.example.loginframe.Service.RejectedAuditService;
import com.example.loginframe.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditDetailService auditDetailService;
    private final AssiginAuditor assiginAuditor;
    private final AuditorService auditorService;
    private final RejectedAuditService rejectedAuditService;

    @PostMapping("/audit-details")
    public ResponseEntity<Map<String, Object>> createAudit(@RequestBody AuditDetailDTO dto) {
        AuditDetails saved = auditDetailService.saveAuditDetail(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("auditId", saved.getAuditId());
        response.put("message", "Audit created successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/audit-details/update/{id}")
    public ResponseEntity<String> updateAudit(@PathVariable Long id, @RequestBody AuditDetailDTO dto) {
        auditDetailService.updateAuditDetail(id, dto);
        return ResponseEntity.ok("Audit Updated Successfully");
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<AuditDetailDTO>> getPendingAudits(Pageable pageable) {
        Page<AuditDetailDTO> audits =
                auditDetailService.getPendingAuditsAndAssigneAuditdsForAdmin(pageable);

        return ResponseEntity.ok(audits);
    }

    @PutMapping("/pending/Assigned/{id}")
    public ResponseEntity<String> assiginAudit(@PathVariable int id, @RequestBody AuditDetailDTO auditDetailDTO) {
        assiginAuditor.assignAuditor((long) id, auditDetailDTO);
        return ResponseEntity.ok("Assigned successfully");
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<String> rejectAudit(@PathVariable Long id, @RequestBody AuditDetailDTO auditDetailDTO) {
        assiginAuditor.rejectAudit(id, auditDetailDTO);
        return ResponseEntity.ok("Rejected successfully");
    }

    @GetMapping("/audit-details/user")
    public ResponseEntity<List<AuditDetailDTO>> getUserNotifications(@RequestParam String loginEmail) {
        return ResponseEntity.ok(auditDetailService.getUserNotifications(loginEmail));
    }

    @GetMapping("/audits")
    public List<AuditDetailDTO> getAudits(@RequestParam(required = false) String status) {
        return auditDetailService.getAudits(status);
    }

    @GetMapping("/counts")
    public AuditCountDTO getAuditCounts() {
        return auditDetailService.getAuditCounts();
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


    @GetMapping("/rejected")
    public ResponseEntity<List<AuditDetailDTO>> getRejectedAudits() {
        return ResponseEntity.ok(rejectedAuditService.getAllRejectedAudits());
    }
}

