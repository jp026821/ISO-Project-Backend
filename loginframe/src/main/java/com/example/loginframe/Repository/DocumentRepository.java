package com.example.loginframe.Repository;

import com.example.loginframe.Entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

<<<<<<< HEAD

public interface DocumentRepository extends JpaRepository<Documents,Long> {

    // It will find the documents by auditdetails id
    List<Documents> findByAuditDetails_AuditId(Long auditId);

    // It will delete the documents by auditdetails id
    void deleteByAuditDetails_AuditId(Long auditId);

    List<Documents> findByAuditDetails_AuditIdAndStatus(Long auditId, String rejected);
}
=======
public interface DocumentRepository extends JpaRepository<Documents, Long> {
    List<Documents> findByAuditDetails_AuditId(Long auditId); // ✅ used by listByAuditId if needed
}
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
