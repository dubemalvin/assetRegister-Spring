package com.malvin.assetregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malvin.assetregister.enums.DepreciationMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private DepreciationMethod depreciationMethod;
    //private int depreciationInterval;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Asset> asset;

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name ="depreciationId")
    private DepreciationRecord depreciation;

    public Category(String name, String description, int depreciationInterval, List<Asset> asset, DepreciationRecord depreciation) {
        this.name = name;
        this.description = description;
//        this.depreciationInterval = depreciationInterval;
        this.asset = asset;
        this.depreciation = depreciation;
    }
}
