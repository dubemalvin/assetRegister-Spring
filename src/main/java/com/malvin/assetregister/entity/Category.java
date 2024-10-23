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

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Asset> asset;

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name ="depreciationId")
    private DepreciationRecord depreciation;
    
}
