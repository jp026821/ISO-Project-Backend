package com.example.loginframe.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditDocumentDTO {
    private Long docId;
    private Long auditId;
    private Long profileId;
    private String loginEmail;

    private String docType;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;
}