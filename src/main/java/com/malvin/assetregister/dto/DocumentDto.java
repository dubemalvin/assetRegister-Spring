package com.malvin.assetregister.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentDto {
    private Long docId;
    private String documentName;
    private String description;
    private String documentType;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String fileType;
    private String downloadUrl;
    private boolean isLifetime;
}
