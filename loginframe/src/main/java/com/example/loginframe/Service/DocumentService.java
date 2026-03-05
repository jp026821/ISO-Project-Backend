package com.example.loginframe.Service;

import com.example.loginframe.Entity.AuditDetails;
import com.example.loginframe.Entity.Documents;
import com.example.loginframe.Repository.AuditDetailsRepository;
import com.example.loginframe.Repository.DocumentRepository;
import com.example.loginframe.dto.DocumentDTO;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Transactional;
=======
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
<<<<<<< HEAD
=======
import java.nio.file.*;
import java.time.LocalDateTime;
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

<<<<<<< HEAD
        @Autowired
        private DocumentRepository documentRepository;

        @Autowired
        private AuditDetailsRepository auditRepository;

        // Rolls back everything if anything fails
        @Transactional
        public Documents saveDocument(Long auditId, MultipartFile file) throws IOException {
            // Step 1: Find audit - if not found, throws exception → rollback
            AuditDetails audit = auditRepository.findById(auditId)
                    .orElseThrow(() -> new RuntimeException("Audit not found with ID: " + auditId));

            // Step 2: Build document
            Documents doc = new Documents();
            doc.setFileName(file.getOriginalFilename());
            doc.setDocType(file.getContentType());
            doc.setData(file.getBytes());
            doc.setAuditDetails(audit);

            // Step 3: Save - if this fails, Step 1 also rolls back
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
=======
    @Autowired private DocumentRepository documentRepository;
    @Autowired private AuditDetailsRepository auditDetailsRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<Documents> uploadMultiple(Long auditId, List<String> docTypes, List<MultipartFile> files) throws IOException {

        if (auditId == null || auditId <= 0) throw new RuntimeException("auditId is required");
        if (files == null || files.isEmpty()) throw new RuntimeException("files are required");

        AuditDetails audit = auditDetailsRepository.findById(auditId)
                .orElseThrow(() -> new RuntimeException("Audit not found with id: " + auditId));

        if (docTypes != null && !docTypes.isEmpty() && docTypes.size() != files.size()) {
            throw new RuntimeException("docTypes count must match files count");
        }

        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        List<Documents> saved = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file == null || file.isEmpty()) continue;

            String storedName = generateStoredFileName(file.getOriginalFilename());
            Path target = dir.resolve(storedName).normalize();

            System.out.println("UPLOAD DIR = " + dir);
            System.out.println("Saving to = " + target);
            System.out.println("Original = " + file.getOriginalFilename() + " size=" + file.getSize());

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            Documents doc = new Documents();
            doc.setAuditDetails(audit);

            doc.setFileName(storedName);
            doc.setOriginalFileName(file.getOriginalFilename());
            doc.setFileType(file.getContentType());
            doc.setFileSize(file.getSize());

            doc.setFilePath(target.toString()); // ✅ fixes: file_path cannot be null

            String category = "GENERAL";
            if (docTypes != null && !docTypes.isEmpty()) category = docTypes.get(i);
            doc.setDocumentCategory(category);

            doc.setVersion(1);
            doc.setUploadedBy(null);
            doc.setUploadedAt(LocalDateTime.now());

            saved.add(documentRepository.save(doc));
        }

        return saved;
    }

    public List<DocumentDTO> listAllDocuments() {
        List<Documents> all = documentRepository.findAll();
        List<DocumentDTO> out = new ArrayList<>();

        for (Documents d : all) {
            Long auditId = (d.getAuditDetails() != null) ? d.getAuditDetails().getAuditId() : null;

            out.add(new DocumentDTO(
                    d.getId(),
                    auditId,
                    d.getDocumentCategory(),
                    d.getOriginalFileName(),
                    d.getFileType(),
                    d.getFileSize(),
                    d.getUploadedAt()
            ));
        }
        return out;
    }

    public Resource downloadResource(Long docId) {
        Documents d = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found: " + docId));

        String filePath = d.getFilePath();
        if (filePath == null || filePath.isBlank()) {
            throw new RuntimeException("filePath missing in DB for docId: " + docId);
        }

        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found on disk: " + path);
        }

        return new FileSystemResource(path.toFile());
    }
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76

    public String getOriginalName(Long docId) {
        Documents d = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found: " + docId));
        return (d.getOriginalFileName() != null && !d.getOriginalFileName().isBlank())
                ? d.getOriginalFileName()
                : d.getFileName();
    }

<<<<<<< HEAD
    // Admin rejects document
    public void rejectDocument(Long docId, String comment) {
        Documents doc = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        doc.setStatus("Rejected");
        doc.setAdminComment(comment);
        documentRepository.save(doc);
=======
    private String generateStoredFileName(String original) {
        String clean = (original == null) ? "file" : original.replaceAll("[^a-zA-Z0-9._-]", "_");
        return System.currentTimeMillis() + "_" + clean;
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
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

