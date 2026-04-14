package com.example.loginframe.Controller;

import com.example.loginframe.Service.FeedbackService;
import com.example.loginframe.dto.FeedbackDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

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
}
