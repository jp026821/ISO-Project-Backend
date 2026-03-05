package com.example.loginframe.dto;

import lombok.AllArgsConstructor;
<<<<<<< HEAD
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private Long id;
    private String fileName;
    private String docType;
    private String status;
    private String adminComment;
=======
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
>>>>>>> 7fde279917cb1acbaa237809eadcf86af259ac76
}