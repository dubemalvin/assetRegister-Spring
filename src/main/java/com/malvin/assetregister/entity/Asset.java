package com.malvin.assetregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malvin.assetregister.enums.AssetStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDate purchaseDate;
    private BigDecimal initialValue;
    private BigDecimal currentValue;
    private double depRate;

    @Enumerated(EnumType.STRING)
    private AssetStatus status;

    @OneToMany(mappedBy = "asset",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Maintenance> maintenanceRecords;

    @JsonIgnore
    @OneToMany(mappedBy = "asset",fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DepreciationRecord> deps;

    public Asset(String name, String description, LocalDate purchaseDate, BigDecimal initialValue,BigDecimal currentValue,double depRate, AssetStatus status, Category category) {
        this.name = name;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.initialValue = initialValue;
        this.currentValue = currentValue;
        this.depRate = depRate;
        this.status = status;
        this.category = category;
    }

}
