package com.malvin.assetregister.dto;

import jakarta.persistence.Lob;
import lombok.Data;

import java.sql.Blob;

@Data
public class ImageDto {
    private Long imageId;
    private String fileName;
    private String downloadUrl;
}
