package com.example.loginframe.Service;

import com.example.loginframe.Entity.AuditDetails;
import com.example.loginframe.Entity.Documents;
import com.example.loginframe.Repository.AuditDetailsRepository;
import com.example.loginframe.Repository.DocumentRepository;
import com.example.loginframe.dto.DocumentDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

        @Autowired
        private DocumentRepository documentRepository;

        @Autowired
        private AuditDetailsRepository auditRepository;

        // Rolls back everything if anything fails
        @Transactional
        public Documents saveDocument(Long auditId, MultipartFile file) throws IOException {
            AuditDetails audit = auditRepository.findById(auditId)
                    .orElseThrow(() -> new RuntimeException("Audit not found with ID: " + auditId));

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("File size exceeds 5 MB limit");
            }

            Documents doc = new Documents();
            doc.setFileName(file.getOriginalFilename());
            doc.setDocType(file.getContentType());
            doc.setData(file.getBytes());
            doc.setStatus("Pending");
            doc.setAuditDetails(audit);

            return documentRepository.save(doc);
        }
        // Read-only transaction (better performance for fetch queries)
        @Transactional(readOnly = true)
        public List<Documents> getDocumentsByAuditId(Long auditId) {
            return documentRepository.findByAuditDetails_AuditId(auditId);
        }

        @Transactional(readOnly = true)
        public Documents getDocumentById(Long docId) {
            return documentRepository.findById(docId)
                    .orElseThrow(() -> new RuntimeException("Document not found"));
        }

    // Admin rejects document
    public void rejectDocument(Long docId, String comment) {
        Documents doc = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        doc.setStatus("Rejected");
        doc.setAdminComment(comment);
        documentRepository.save(doc);

    }

    // Admin approves document
    public void approveDocument(Long docId) {
        Documents doc = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        doc.setStatus("Approved");
        doc.setAdminComment(null);
        documentRepository.save(doc);
    }



    // User re-uploads - reset status back to Pending
    public Documents reUploadDocument(Long docId, MultipartFile file) throws IOException {
        Documents doc = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        doc.setFileName(file.getOriginalFilename());
        doc.setDocType(file.getContentType());
        doc.setData(file.getBytes());
        doc.setStatus("Resubmitted");       // ✅ back to pending so admin reviews again
        doc.setAdminComment(null);      // ✅ clear old comment
        return documentRepository.save(doc);
    }



    public List<DocumentDTO> getRejectedDocumentsByAuditId(Long auditId) {
        return documentRepository
                .findByAuditDetails_AuditIdAndStatus(auditId, "Rejected")
                .stream()
                .map(doc -> new DocumentDTO(
                        doc.getId(),
                        doc.getFileName(),
                        doc.getDocType(),
                        doc.getStatus(),
                        doc.getAdminComment()
                ))
                .toList();    }



    }

