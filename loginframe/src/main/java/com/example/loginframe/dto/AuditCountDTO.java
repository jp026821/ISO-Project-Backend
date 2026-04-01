package com.example.loginframe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditCountDTO {

    private long assignedAudits;
    private long inProgressAudits;
    private long completedAudits;
}
