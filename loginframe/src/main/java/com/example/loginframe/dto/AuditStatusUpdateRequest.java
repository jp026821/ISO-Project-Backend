package com.example.loginframe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditStatusUpdateRequest {
    private String auditorEmail;
    private String status;
    private String message;
}