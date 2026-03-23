package com.example.loginframe.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_updates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audit_id", nullable = false)
    private Long auditId;

    @Column(name = "updated_by", nullable = false, length = 150)
    private String updatedBy;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "status_after_update", length = 100)
    private String statusAfterUpdate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}