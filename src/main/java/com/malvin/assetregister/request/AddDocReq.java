package com.malvin.assetregister.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddDocReq {
    private String documentName;
    private String description;
    private String documentType;
    @JsonFormat(pattern = "yyyy-MM-dd") // Adjust pattern as needed
    private LocalDate issueDate;
    @JsonFormat(pattern = "yyyy-MM-dd") // Adjust pattern as needed
    private LocalDate expiryDate;
    private String fileType;
    private String downloadUrl;
    private boolean isLifetime;
}
