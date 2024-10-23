package com.malvin.assetregister.service.depreciation;

import com.malvin.assetregister.entity.DepreciationRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IDepreciationService {
    void runScheduledDepreciation();

    List<DepreciationRecord> findAllByAssetId(Long assetId);

    List<DepreciationRecord> findAll();

    List<DepreciationRecord> findDepreciationRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<DepreciationRecord> findDepreciationRecordsByAssetAndDateRange(Long assetId, LocalDateTime from, LocalDateTime to);

    LocalDateTime lastDepreciation(Long assetId);

    BigDecimal getCostOfMaintenanceSinceLastDepreciation(Long assetId);
}
