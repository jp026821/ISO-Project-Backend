package com.example.loginframe.Controller;

import com.example.loginframe.Service.ContactService;
import com.example.loginframe.dto.ContactDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

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
}
