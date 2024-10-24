package com.malvin.assetregister.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DepreciationDto {
    private LocalDateTime calculationDate;
    private BigDecimal depreciationAmount;
    private BigDecimal currentValue;
    private BigDecimal precedingValue;

//    @JsonIgnore
    private AssetDto asset;

    @JsonIgnore
    private CategoryDto category;
}
