package com.malvin.assetregister.service.depreciation;

import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.DepreciationRecord;
import com.malvin.assetregister.entity.Maintenance;
import com.malvin.assetregister.enums.DepreciationMethod;
import com.malvin.assetregister.enums.MaintenanceStatus;
import com.malvin.assetregister.repository.AssetRepository;
import com.malvin.assetregister.repository.DepreciationRecordRepository;
import com.malvin.assetregister.repository.MaintenanceRepository;
import com.malvin.assetregister.service.asset.IAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepreciationService implements IDepreciationService {
    private final DepreciationRecordRepository depreciationRecordRepository;
    private final AssetRepository assetRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final IAssetService assetService;

    @Async
    @Scheduled(cron = "*/60 * * * * *")
    @Override
    public void runScheduledDepreciation() {
            List<Asset> assets =assetService.getAllAssets();
            assets.forEach(asset -> {
                BigDecimal depreciationAmount = calculateDepreciation(asset);
                if (depreciationAmount != null) {
                    BigDecimal precedingValue =asset.getCurrentValue();
                    BigDecimal currentValue = precedingValue.subtract(depreciationAmount);
                    asset.setCurrentValue(currentValue);
                    assetRepository.save(asset);
                    DepreciationRecord record = new DepreciationRecord(LocalDateTime.now(), depreciationAmount, currentValue, precedingValue, asset);
                    depreciationRecordRepository.save(record);
                }
            });
    }

    @Override
    public List<DepreciationRecord> findAllByAssetId(Long assetId) {
        return depreciationRecordRepository.findByAsset(assetService.getAssetById(assetId));
    }

    @Override
    public List<DepreciationRecord> findAll() {
        return depreciationRecordRepository.findAll();
    }

    @Override
    public List<DepreciationRecord> findDepreciationRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return depreciationRecordRepository.findByCalculationDateBetween(startDate,endDate);
    }

    @Override
    public List<DepreciationRecord> findDepreciationRecordsByAssetAndDateRange(Long assetId, LocalDateTime from, LocalDateTime to){

        if (from.isBefore(LocalDateTime.now()) && to.isAfter(from)) {
            Asset asset = assetService.getAssetById(assetId);
            return depreciationRecordRepository.findDepreciationRecordByAssetAndDateRange(asset, from, to);
        }
        return null;
    }

    @Override
    public LocalDateTime lastDepreciation(Long assetId) {
        Asset asset = assetService.getAssetById(assetId);
        return asset.getDeps()
                .stream()
                .map(DepreciationRecord::getCalculationDate)
                .reduce((first, second) -> second) // Get the last element if present
                .orElse(LocalDateTime.now()); // Default value if no records
    }

    @Override
    public BigDecimal getCostOfMaintenanceSinceLastDepreciation(Long assetId) {
        Asset asset = assetService.getAssetById(assetId);
        LocalDateTime dateTime = lastDepreciation(assetId);
        List<Maintenance> maintenances = maintenanceRepository.findByAsset(asset);

        return maintenances
                .stream()
                .filter(maintenance -> {
                    LocalDateTime completionDate = maintenance.getDayOfCompletion();
                    return completionDate != null &&
                            completionDate.isAfter(dateTime) &&
                            (maintenance.getStatus() == MaintenanceStatus.OPERATIONAL ||
                                    maintenance.getStatus() == MaintenanceStatus.REPAIRED);
                })
                .map(maintenance -> maintenance.getCost() != null ? maintenance.getCost() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDepreciation(Asset asset) {
        BigDecimal depreciationAmount;
        BigDecimal currentValue = asset.getCurrentValue()
                .add(getCostOfMaintenanceSinceLastDepreciation(asset.getId()));
        double depreciationRate = asset.getDepRate();

        DepreciationMethod method = asset.getCategory().getDepreciationMethod();
        switch (method){
            case SLD -> depreciationAmount=currentValue.multiply(BigDecimal.valueOf(depreciationRate));
            case DBB -> depreciationAmount=currentValue.add(BigDecimal.valueOf(depreciationRate));
            default ->
                throw new RuntimeException("Unsupported depreciation method");
        }
        return depreciationAmount;
    }
}
