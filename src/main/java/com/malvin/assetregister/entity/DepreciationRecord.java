package com.malvin.assetregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class DepreciationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime calculationDate;
    private BigDecimal depreciationAmount;
    private BigDecimal currentValue;
    private BigDecimal precedingValue;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public DepreciationRecord(LocalDateTime calculationDate, BigDecimal depreciationAmount, BigDecimal currentValue, BigDecimal precedingValue, Asset asset) {
        this.calculationDate = calculationDate;
        this.depreciationAmount = depreciationAmount;
        this.currentValue = currentValue;
        this.precedingValue = precedingValue;
        this.asset = asset;
    }
}
