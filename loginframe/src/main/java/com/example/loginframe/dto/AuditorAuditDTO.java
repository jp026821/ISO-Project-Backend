package com.example.loginframe.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditorAuditDTO {
    private Long auditId;
    private String auditType;
    private String preferredDate;
    private String duration;
    private String auditLocation;
    private String scope;
    private String notes;
    private String status;
    private String assignedAuditor;
    private String auditorComment;
    private String adminComment;
    private String loginEmail;
    private List<String> isoStandards;
    private List<DocumentDTO> documents;
}