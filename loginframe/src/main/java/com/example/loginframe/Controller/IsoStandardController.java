package com.example.loginframe.Controller;

import com.example.loginframe.Service.IsoStandardService;
import com.example.loginframe.dto.IsoStandardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/iso-standards")
public class IsoStandardController {

    private final IsoStandardService isoStandardService;

    @GetMapping("/")
    public ResponseEntity<List<IsoStandardDTO>> getAllIsoStandard() {
        return ResponseEntity.ok(isoStandardService.getAllIsoStandared());
    }

    @PostMapping("/create")
    public ResponseEntity<String> addIsoStandard(@RequestBody IsoStandardDTO dto) {
        isoStandardService.addIsoStandard(dto);
        return ResponseEntity.ok("ISO Standard saved successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateIsoStandard(@PathVariable Long id, @RequestBody IsoStandardDTO dto) {
        isoStandardService.updateIsoStandard(id, dto);
        return ResponseEntity.ok("ISO updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteIsoStandard(@PathVariable Long id) {
        isoStandardService.deleteIsoStandard(id);
        return ResponseEntity.ok("ISO deleted successfully");
    }
}
