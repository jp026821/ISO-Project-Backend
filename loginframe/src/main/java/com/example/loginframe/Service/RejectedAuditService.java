package com.example.loginframe.Service;

import com.example.loginframe.Entity.AuditDetails;
import com.example.loginframe.Entity.IsoStandard;
import com.example.loginframe.Repository.AuditDetailsRepository;
import com.example.loginframe.dto.AuditDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RejectedAuditService {

    private final AuditDetailsRepository auditDetailsRepository;

    public List<AuditDetailDTO> getAllRejectedAudits() {
        return auditDetailsRepository.findByStatusIgnoreCaseOrderByAuditIdDesc("Rejected")
                .stream()
                .map(this::toDto)
                .toList();
    }

    private AuditDetailDTO toDto(AuditDetails audit) {
        AuditDetailDTO dto = new AuditDetailDTO();

        dto.setAuditId(audit.getAuditId());
        dto.setAuditType(audit.getAuditType());
        dto.setPreferredDate(audit.getPreferredDate());
        dto.setDuration(audit.getDuration());
        dto.setAuditLocation(audit.getAuditLocation());
        dto.setScope(audit.getScope());
        dto.setNotes(audit.getNotes());
        dto.setStatus(audit.getStatus());
        dto.setAssignedAuditor(audit.getAssignedAuditor());
        dto.setAdminComment(audit.getAdminComment());

        if (audit.getProfile() != null) {
            dto.setLoginEmail(audit.getProfile().getLoginEmail());
        }

        List<String> isoCodes = (audit.getIsoStandards() == null)
                ? List.of()
                : audit.getIsoStandards().stream()
                .map(IsoStandard::getIsoCode)
                .toList();

        dto.setIsoStandards(isoCodes);

        return dto;
    }
}
