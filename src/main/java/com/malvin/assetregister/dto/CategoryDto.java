package com.malvin.assetregister.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malvin.assetregister.enums.DepreciationMethod;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private String name;
    private String description;
    private DepreciationMethod depreciationMethod;

    @JsonIgnore
    private List<AssetDto> assetDtos;
}
