package com.malvin.assetregister.request;

import com.malvin.assetregister.entity.Category;
import com.malvin.assetregister.enums.AssetStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddAssetReq {
    private String name;
    private String description;
    private LocalDate purchaseDate;
    private BigDecimal initialValue;
    private double depRate;
    private String category;
    private AssetStatus status;
}
