package com.malvin.assetregister.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateAssetReq {
    private String name;
    private String description;
    private LocalDate purchaseDate;
    private BigDecimal initialValue;
    private double depRate;
    private String category;
}
