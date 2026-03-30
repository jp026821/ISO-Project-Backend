package com.example.loginframe.Controller;

import com.example.loginframe.Entity.AuditDetails;
import com.example.loginframe.Entity.Documents;
import com.example.loginframe.Entity.ProfileEntity;
import com.example.loginframe.Entity.ProfileOrganizationRequest;
import com.example.loginframe.Service.*;
import com.example.loginframe.dto.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.loginframe.Entity.AuditUpdate;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthanticationController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SignupService signupService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeSignupService employeSignupService;

    @Autowired
    private IsoStandardService isoStandardService;

    @Autowired
    private AuditDetailService auditDetailService;

    @Autowired
    private AssiginAuditor assiginAuditor;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private AuditorService auditorService;

    @Autowired
    private FeedbackService feedbackService;

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
        String result = signupService.signup(request);

        if (result.equals("Signup successful")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
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

    /* ================= ADMIN: PENDING AUDITS ================= */
    @GetMapping("/pending")
    public ResponseEntity<Page<AuditDetailDTO>> getPendingAudits(Pageable pageable) {

        Page<AuditDetailDTO> audits =
                auditDetailService.getPendingAuditsAndAssigneAuditdsForAdmin(pageable);

        return ResponseEntity.ok(audits);   // ✅ HTTP 200
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

    /* ================= USER: UPLOAD DOCUMENTS ================= */
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

    @PutMapping("/audit/documents/{docId}/reupload")
    public ResponseEntity<Documents> reUploadDocument(
            @PathVariable Long docId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        Documents updatedDoc = documentService.reUploadDocument(docId, file);
        return ResponseEntity.ok(updatedDoc);
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

    /* ================= CONTACT ================= */
    @PostMapping("/contact")
    public ResponseEntity<?> saveContact(@Valid @RequestBody ContactDto contactDto) {
        try {
            ContactDto savedContact = contactService.saveContact(contactDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save contact details");
        }
    }

    /* ================= FEEDBACK ================= */
    @PostMapping("/feedback")
    public ResponseEntity<?> saveFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        try {
            FeedbackDto saved = feedbackService.saveFeedback(feedbackDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save feedback: " + e.getMessage());
        }
    }

    @GetMapping("/feedback")
    public ResponseEntity<List<FeedbackDto>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    /* ================= AUDITOR ================= */
    @GetMapping("/my-audits")
    public List<AuditorAuditDTO> getAssignedAudits(@RequestParam String auditorEmail) {
        return auditorService.getAssignedAudits(auditorEmail);
    }

    @GetMapping("/audit/{auditId}")
    public AuditorAuditDTO getAuditDetails(
            @PathVariable Long auditId,
            @RequestParam String auditorEmail
    ) {
        return auditorService.getAuditDetails(auditId, auditorEmail);
    }

    @GetMapping("/audit/{auditId}/updates")
    public List<AuditUpdate> getAuditUpdates(
            @PathVariable Long auditId,
            @RequestParam String auditorEmail
    ) {
        return auditorService.getAuditUpdates(auditId, auditorEmail);
    }

    @PostMapping("/audit/{auditId}/remark")
    public String addRemark(
            @PathVariable Long auditId,
            @RequestBody AuditRemarkRequest request
    ) {
        return auditorService.addRemark(auditId, request);
    }

    @PutMapping("/audit/{auditId}/status")
    public String updateStatus(
            @PathVariable Long auditId,
            @RequestBody AuditStatusUpdateRequest request
    ) {
        return auditorService.updateAuditStatus(auditId, request);
    }

    /* ================= USER PRODUCTS ================= */
    @GetMapping("/user/products")
    public ResponseEntity<List<String>> getProducts() {
        return ResponseEntity.ok(List.of(
                "ISO Certification",
                "Internal Audit",
                "External Audit"
        ));
    }
}