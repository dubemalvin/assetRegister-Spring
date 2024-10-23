package com.malvin.assetregister.repository;

import com.malvin.assetregister.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByInitialValueBetween(BigDecimal min, BigDecimal max);

    Integer countByCategoryName(String category);
}
