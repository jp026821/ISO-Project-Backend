package com.example.loginframe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditRemarkRequest {
    private String auditorEmail;
    private String remark;
}