package com.example.loginframe.Service;

import com.example.loginframe.Entity.AuditDetails;
import com.example.loginframe.Repository.AuditDetailsRepository;
import com.example.loginframe.dto.AuditDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssiginAuditor {

    @Autowired
    private AuditDetailsRepository auditDetailsRepository;

    public void assignAuditor(Long auditId, AuditDetailDTO dto) {

        AuditDetails audit = auditDetailsRepository.findById(auditId)
                .orElseThrow(() -> new RuntimeException("Audit not found"));

        if (dto.getAssignedAuditor() != null && !dto.getAssignedAuditor().trim().isEmpty()) {
            audit.setAssignedAuditor(dto.getAssignedAuditor().trim());
        }

        String status = (dto.getStatus() == null || dto.getStatus().trim().isEmpty())
                ? "Assigned"
                : dto.getStatus().trim();

        audit.setStatus(status);
        audit.setAdminComment(dto.getAdminComment());

        auditDetailsRepository.save(audit);
    }

    public void rejectAudit(Long auditId, AuditDetailDTO dto) {

        AuditDetails audit = auditDetailsRepository.findById(auditId)
                .orElseThrow(() -> new RuntimeException("Audit not found"));

        audit.setStatus("Rejected");
        audit.setAdminComment(dto.getAdminComment());

        auditDetailsRepository.save(audit);
    }
}