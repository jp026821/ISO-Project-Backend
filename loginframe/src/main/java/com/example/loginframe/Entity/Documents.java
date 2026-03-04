package com.example.loginframe.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // stored file name on disk
    @Column(nullable = false)
    private String fileName;

    private String originalFileName;
    private String fileType;
    private Long fileSize;

    @Column(name = "file_path", nullable = false) // ✅ your DB requires NOT NULL
    private String filePath;

    private String documentCategory;
    private Integer version;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditDetails auditDetails;
}