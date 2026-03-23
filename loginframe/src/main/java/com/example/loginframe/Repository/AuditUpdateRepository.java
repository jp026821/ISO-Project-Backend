package com.example.loginframe.Repository;

import com.example.loginframe.Entity.AuditUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditUpdateRepository extends JpaRepository<AuditUpdate, Long> {
    List<AuditUpdate> findByAuditIdOrderByCreatedAtDesc(Long auditId);
}