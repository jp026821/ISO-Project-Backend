package com.example.loginframe.Controller;

import com.example.loginframe.Entity.ProfileEntity;
import com.example.loginframe.Entity.ProfileOrganizationRequest;
import com.example.loginframe.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Transactional
    @PostMapping("/saveorupdate")
    public ResponseEntity<String> saveOrUpdateProfile(@RequestBody ProfileOrganizationRequest porequest) {
        try {
            return ResponseEntity.ok(profileService.saveOrUpdateProfile(porequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/getall")
    public ResponseEntity<?> getProfileGetAll(@RequestParam String loginEmail) {
        try {
            ProfileEntity profile = profileService.getByLoginEmail(loginEmail);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }
    }
}
