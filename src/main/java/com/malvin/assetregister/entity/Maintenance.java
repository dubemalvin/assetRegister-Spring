package com.malvin.assetregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.enums.MaintenanceStatus;
import com.malvin.assetregister.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private Asset asset;
    private LocalDateTime today;
    private LocalDateTime scheduledDate;       // Date for the next scheduled maintenance
    private LocalDateTime performedDate;       // Date the maintenance was performed
    private LocalDateTime dayOfCompletion;
    private String details;                // Maintenance or repair details
    private BigDecimal cost;               // Cost associated with the maintenance
    @Enumerated(EnumType.STRING)
    private MaintenanceType type;          // Type: MAINTENANCE or REPAIR
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;      // Status of the maintenance
    private int downtime;                  // Downtime in days for the asset due to maintenance

    public Maintenance(Asset asset,LocalDateTime today, LocalDateTime scheduledDate, LocalDateTime performedDate,
                       LocalDateTime dayOfCompletion, String details, BigDecimal cost, MaintenanceType type,
                       MaintenanceStatus status, int downtime) {
        this.asset = asset;
        this.today= today;
        this.scheduledDate = scheduledDate;
        this.performedDate = performedDate;
        this.dayOfCompletion= dayOfCompletion;
        this.details = details;
        this.cost = cost;
        this.type = type;
        this.status = status;
        this.downtime = downtime;
    }
}
