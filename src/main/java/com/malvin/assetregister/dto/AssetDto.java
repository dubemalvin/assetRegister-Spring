package com.malvin.assetregister.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.malvin.assetregister.enums.AssetStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class AssetDto {
    private String name;
    private String description;
    private LocalDate purchaseDate;
    private BigDecimal initialValue;
    private BigDecimal currentValue;
    private double depRate;

    private AssetStatus status;

    private List<ImageDto> image;

    private CategoryDto category;
}
