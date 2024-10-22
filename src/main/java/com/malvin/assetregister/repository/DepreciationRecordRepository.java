package com.malvin.assetregister.repository;

import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.DepreciationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DepreciationRecordRepository extends JpaRepository<DepreciationRecord, Long> {

    List<DepreciationRecord> findByAsset(Asset asset);

    @Query("SELECT d FROM DepreciationRecord d WHERE d.asset = :asset AND d.calculationDate BETWEEN :from AND :to")
    List<DepreciationRecord> findDepreciationRecordByAssetAndDateRange(
            @Param("asset") Asset asset,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    List<DepreciationRecord> findByCalculationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
