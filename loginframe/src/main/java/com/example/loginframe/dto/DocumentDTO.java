package com.example.loginframe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    private Long id;
    private Long auditId;

    private String documentCategory;
    private String originalFileName;
    private String fileType;
    private Long fileSize;

    private LocalDateTime uploadedAt;
}