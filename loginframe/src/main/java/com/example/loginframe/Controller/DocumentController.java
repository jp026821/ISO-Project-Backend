package com.example.loginframe.Controller;

import com.example.loginframe.Entity.Documents;
import com.example.loginframe.Service.DocumentService;
import com.example.loginframe.dto.DocumentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/{auditId}/documents/upload")
    public ResponseEntity<String> uploadDocument(
            @PathVariable Long auditId,
            @RequestParam("file") MultipartFile file) {
        try {
            Documents saved = documentService.saveDocument(auditId, file);
            return ResponseEntity.ok("Document saved with ID: " + saved.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    @GetMapping("/{auditId}/documents")
    public ResponseEntity<List<Map<String, Object>>> getDocuments(@PathVariable Long auditId) {
        List<Documents> docs = documentService.getDocumentsByAuditId(auditId);

        List<Map<String, Object>> response = docs.stream().map(doc -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", doc.getId());
            map.put("fileName", doc.getFileName());
            map.put("fileType", doc.getDocType());
            map.put("downloadUrl", "/api/" + auditId + "/documents/" + doc.getId());
            map.put("status", doc.getStatus());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{auditId}/documents/{docId}")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long auditId, @PathVariable Long docId) {
        Documents doc = documentService.getDocumentById(docId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getDocType()))
                .body(doc.getData());
    }

    @GetMapping("/{auditId}/documents/rejected")
    public ResponseEntity<List<DocumentDTO>> getRejectedDocuments(@PathVariable Long auditId) {
        return ResponseEntity.ok(documentService.getRejectedDocumentsByAuditId(auditId));
    }

    @PutMapping("/{auditId}/documents/{docId}/reject")
    public ResponseEntity<String> rejectDocument(@PathVariable Long auditId,
                                                 @PathVariable Long docId,
                                                 @RequestBody Map<String, String> body) {
        try {
            String comment = body.get("adminComment");
            documentService.rejectDocument(docId, comment);
            return ResponseEntity.ok("Document rejected");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/{auditId}/documents/{docId}/approve")
    public ResponseEntity<String> approveDocument(@PathVariable Long auditId, @PathVariable Long docId) {
        try {
            documentService.approveDocument(docId);
            return ResponseEntity.ok("Document approved");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/audit/documents/{docId}/reupload")
    public ResponseEntity<Documents> reUploadDocument(
            @PathVariable Long docId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Documents updatedDoc = documentService.reUploadDocument(docId, file);
        return ResponseEntity.ok(updatedDoc);
    }
}
