package com.example.loginframe.Controller;

import com.example.loginframe.Service.LoginService;
import com.example.loginframe.Service.SignupService;
import com.example.loginframe.dto.LoginRequest;
import com.example.loginframe.dto.LoginResponse;
import com.example.loginframe.dto.SignupRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;

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
}