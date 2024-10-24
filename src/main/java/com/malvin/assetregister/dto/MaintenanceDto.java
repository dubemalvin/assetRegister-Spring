package com.malvin.assetregister.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malvin.assetregister.enums.MaintenanceStatus;
import com.malvin.assetregister.enums.MaintenanceType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaintenanceDto {

    private LocalDateTime today;
    private LocalDateTime scheduledDate;
    private LocalDateTime performedDate;
    private LocalDateTime dayOfCompletion;
    private String details;
    private BigDecimal cost;
    private int downtime;

    private MaintenanceType type;

    private MaintenanceStatus status;

    @JsonIgnore
    private AssetDto assetDto;
}
