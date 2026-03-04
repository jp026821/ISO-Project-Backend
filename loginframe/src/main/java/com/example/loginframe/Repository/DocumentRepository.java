package com.example.loginframe.Repository;

import com.example.loginframe.Entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Documents, Long> {
    List<Documents> findByAuditDetails_AuditId(Long auditId); // ✅ used by listByAuditId if needed
}