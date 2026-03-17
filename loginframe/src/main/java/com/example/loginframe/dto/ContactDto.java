package com.example.loginframe.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDto {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String companyName;
    private String message;
    private List<String> isoStandardCodes;
}