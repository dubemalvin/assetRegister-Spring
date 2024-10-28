package com.malvin.assetregister.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentName;
    private String description;
    private String documentType;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String fileType;
    private String downloadUrl;
    private boolean isLifetime;

    @Lob
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "assetId")
    private Asset asset;


    
}
