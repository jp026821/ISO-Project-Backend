package com.example.loginframe.Entity;

import jakarta.persistence.*;
import lombok.*;


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

<<<<<<< HEAD
        private String fileName;
        private String docType;

        @Lob
        @Column(columnDefinition = "LONGBLOB")
        private byte[] data;

        private String status = "Pending";   // Pending, Approved, Rejected
        private String adminComment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "audit_id", nullable = false)
        private AuditDetails auditDetails;


}
=======
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
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
