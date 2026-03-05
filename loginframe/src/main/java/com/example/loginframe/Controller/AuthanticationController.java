package com.example.loginframe.Controller;

import com.example.loginframe.Entity.AuditDetails;
import com.example.loginframe.Entity.Documents;
import com.example.loginframe.Entity.ProfileEntity;
import com.example.loginframe.Entity.ProfileOrganizationRequest;
import com.example.loginframe.Service.*;
import com.example.loginframe.dto.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
=======
import org.springframework.core.io.Resource;
import org.springframework.http.*;
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
=======
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthanticationController {

    @Autowired private ProfileService profileService;
    @Autowired private SignupService signupService;
    @Autowired private LoginService loginService;
    @Autowired private UserService userService;
    @Autowired private EmployeSignupService employeSignupService;
    @Autowired private IsoStandardService isoStandardService;
    @Autowired private AuditDetailService auditDetailService;
    @Autowired private AssiginAuditor assiginAuditor;
    @Autowired private DocumentService documentService;

    /* ================= LOGIN ================= */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        return ResponseEntity.ok(loginService.login(request, session));
    }

    /* ================= LOGOUT ================= */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }

    /* ================= SIGNUP ================= */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        signupService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Signup successful");
    }

    /* ================= PROFILE ================= */
    @Transactional
    @PostMapping("/profile")
    public ResponseEntity<String> saveOrUpdateProfile(@RequestBody ProfileOrganizationRequest porequest) {
        try {
            return ResponseEntity.ok(profileService.saveOrUpdateProfile(porequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String loginEmail) {
        try {
            ProfileEntity profile = profileService.getByLoginEmail(loginEmail);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }
    }

    /* ================= USERS ================= */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<AdminUserResponse> updateUser(@PathVariable int id, @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(userService.updateUser(id, req));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    /* ================= ADMIN ADD EMPLOYEE ================= */
    @PostMapping("/admin/add-employee")
    public ResponseEntity<String> signupEmployeeOnly(@RequestBody SignupRequest request) {
        try {
            return ResponseEntity.ok(employeSignupService.addEmployee(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    /* ================= ISO STANDARDS ================= */
    @GetMapping("/iso-standards")
    public ResponseEntity<List<IsoStandardDTO>> getAllIsoStandard() {
        return ResponseEntity.ok(isoStandardService.getAllIsoStandared());
    }

    @PostMapping("/iso-standards/create")
    public ResponseEntity<String> addIsoStandard(@RequestBody IsoStandardDTO dto) {
        isoStandardService.addIsoStandard(dto);
        return ResponseEntity.ok("ISO Standard saved successfully");
    }

    @PutMapping("/iso-standards/update/{id}")
    public ResponseEntity<String> updateIsoStandard(@PathVariable Long id, @RequestBody IsoStandardDTO dto) {
        isoStandardService.updateIsoStandard(id, dto);
        return ResponseEntity.ok("ISO updated successfully");
    }

    @DeleteMapping("/iso-standards/delete/{id}")
    public ResponseEntity<String> deleteIsoStandard(@PathVariable Long id) {
        isoStandardService.deleteIsoStandard(id);
        return ResponseEntity.ok("ISO deleted successfully");
    }

    /* ================= AUDIT DETAILS ================= */
    @PostMapping("/audit-details")
<<<<<<< HEAD
    public ResponseEntity<Map<String, Object>> createAudit(@RequestBody AuditDetailDTO dto) {
        AuditDetails saved = auditDetailService.saveAuditDetail(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("auditId", saved.getAuditId());
        response.put("message", "Audit created successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/audit-details/update/{id}")
    public ResponseEntity<String> updateAudit(@PathVariable Long id, @RequestBody AuditDetailDTO dto) {
        auditDetailService.updateAuditDetail(id, dto);
        return ResponseEntity.ok("Audit Updated Successfully");
    }

    @DeleteMapping("/audit-details/delete/{id}")
    public ResponseEntity<String> deleteAudit(@PathVariable long id) {
        auditDetailService.deleteAudit(id);
        return ResponseEntity.ok("Audit Deleted Successfully");
=======
    public ResponseEntity<?> createAudit(@RequestBody AuditDetailDTO dto) {
        AuditDetails saved = auditDetailService.saveAuditDetail(dto);
        return ResponseEntity.ok(Map.of(
                "auditId", saved.getAuditId(),
                "message", "Audit created successfully"
        ));
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
    }

    /* ================= ADMIN: PENDING AUDITS ================= */
    @GetMapping("/pending")
    public ResponseEntity<List<AuditDetailDTO>> getpendingaudit() {
        return ResponseEntity.ok(auditDetailService.getPendingAuditsForAdmin());
    }

    @PutMapping("/pending/Assigned/{id}")
    public ResponseEntity<String> assiginAudit(@PathVariable int id, @RequestBody AuditDetailDTO auditDetailDTO) {
        assiginAuditor.assignAuditor((long) id, auditDetailDTO);
        return ResponseEntity.ok("Assigned successfully");
    }

    /* ================= USER: NOTIFICATIONS ================= */
    @GetMapping("/audit-details/user")
    public ResponseEntity<List<AuditDetailDTO>> getUserNotifications(@RequestParam String loginEmail) {
        return ResponseEntity.ok(auditDetailService.getUserNotifications(loginEmail));
    }

<<<<<<< HEAD
    /*===================== USER: UPLOAD DOCUMENTS =================== */
    @PostMapping("/{auditId}/documents/upload")
    public ResponseEntity<String> uploadDocument(@PathVariable Long auditId,@RequestParam("file") MultipartFile file)
    {
        try {
            Documents saved = documentService.saveDocument(auditId, file);
            return ResponseEntity.ok("Document saved with ID: " + saved.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
=======
    /* ================= DOCUMENT UPLOAD (MULTI) ================= */
    // React calls: POST /api/audit-documents/upload-multi
    @PostMapping(value = "/audit-documents/upload-multi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultipleDocuments(
            @RequestParam("auditId") Long auditId,
            @RequestParam(value = "docTypes", required = false) List<String> docTypes,
            @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            List<Documents> saved = documentService.uploadMultiple(auditId, docTypes, files);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* ================= ADMIN: LIST ALL DOCUMENTS ================= */
    // Admin calls: GET /api/admin/documents
    @GetMapping("/admin/documents")
    public ResponseEntity<?> adminListAllDocuments() {
        return ResponseEntity.ok(documentService.listAllDocuments());
    }

    /* ================= ADMIN: DOWNLOAD DOCUMENT ================= */
    // Admin calls: GET /api/admin/documents/download/{docId}
    @GetMapping("/admin/documents/download/{docId}")
    public ResponseEntity<?> downloadDocument(@PathVariable Long docId) {
        try {
            Resource resource = documentService.downloadResource(docId);
            String originalName = documentService.getOriginalName(docId);

            String contentType = "application/octet-stream";
            try {
                String probed = Files.probeContentType(resource.getFile().toPath());
                if (probed != null) contentType = probed;
            } catch (Exception ignored) {}

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalName + "\"")
                    .body(resource);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
        }
    }

    // Get all documents for a specific audit
    @GetMapping("/{auditId}/documents")
    public ResponseEntity<List<Map<String, Object>>> getDocuments(@PathVariable Long auditId) {
        List<Documents> docs = documentService.getDocumentsByAuditId(auditId);

        // Return metadata only (not binary data in list)
        List<Map<String, Object>> response = docs.stream().map(doc -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", doc.getId());
            map.put("fileName", doc.getFileName());
            map.put("fileType", doc.getDocType());
            map.put("downloadUrl", "/api/audit/" + auditId + "/documents/" + doc.getId());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Download/view a specific document
    @GetMapping("/{auditId}/documents/{docId}")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long auditId,@PathVariable Long docId)
    {
        Documents doc = documentService.getDocumentById(docId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getDocType()))
                .body(doc.getData());
    }

    // Show User All Rejected Documents
    @GetMapping("/{auditId}/documents/rejected")
    public ResponseEntity<List<DocumentDTO>> getRejectedDocuments(@PathVariable Long auditId) {
        return ResponseEntity.ok(documentService.getRejectedDocumentsByAuditId(auditId));
    }


    // Admin rejects a document with comment
    @PutMapping("/{auditId}/documents/{docId}/reject")
    public ResponseEntity<String> rejectDocument(@PathVariable Long auditId, @PathVariable Long docId,
            @RequestBody Map<String, String> body) {
        try {
            String comment = body.get("adminComment");
            documentService.rejectDocument(docId, comment);
            return ResponseEntity.ok("Document rejected");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // Admin approves a document
    @PutMapping("/{auditId}/documents/{docId}/approve")
    public ResponseEntity<String> approveDocument(@PathVariable Long auditId, @PathVariable Long docId) {
        try {
            documentService.approveDocument(docId);
            return ResponseEntity.ok("Document approved");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }





}