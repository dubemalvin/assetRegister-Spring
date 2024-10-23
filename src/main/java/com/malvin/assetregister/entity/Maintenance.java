package com.malvin.assetregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malvin.assetregister.enums.MaintenanceStatus;
import com.malvin.assetregister.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime today;
    private LocalDateTime scheduledDate;
    private LocalDateTime performedDate;
    private LocalDateTime dayOfCompletion;
    private String details;
    private BigDecimal cost;
    private int downtime;

    @Enumerated(EnumType.STRING)
    private MaintenanceType type;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private Asset asset;

}
