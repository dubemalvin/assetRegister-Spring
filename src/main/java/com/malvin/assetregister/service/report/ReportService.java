package com.malvin.assetregister.service.report;

import com.malvin.assetregister.entity.*;
import com.malvin.assetregister.enums.MaintenanceStatus;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.repository.CategoryRepository;
import com.malvin.assetregister.service.asset.IAssetService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final CategoryRepository categoryRepository;
    private final IAssetService assetService;

    @Override
    public Map<String, Object> generateReport(@NotNull String reportType, String filter, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return switch (reportType.toLowerCase()) {
                case "category" -> getCategoryReport(filter, startDate, endDate);
                case "depreciation" -> getDepreciationReport();
                case "maintenance" -> getMaintenanceReport(startDate, endDate);
                case "asset" -> getAssetReport(Long.parseLong(filter));
                default -> throw new ResourceNotFoundException("Invalid report type");
            };
        } catch (NumberFormatException | ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Map<String, Object> getCategoryReport(String filter, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            Category category = Optional.ofNullable(categoryRepository.findByName(filter))
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            BigDecimal currentCost = category.getAsset()
                    .stream()
                    .map(Asset::getCurrentValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int downtime = category.getAsset().stream()
                    .flatMap(asset -> asset.getMaintenanceRecords()
                            .stream()
                            .filter(record -> {
                                LocalDateTime completionDate = record.getDayOfCompletion(); // Capture the completion date
                                return completionDate != null && // Check for null
                                        completionDate.isAfter(startDate.toLocalDate().atStartOfDay()) &&
                                        completionDate.isBefore(endDate.toLocalDate().atStartOfDay());
                            }))
                    .mapToInt(Maintenance::getDowntime) // Assuming getDowntime() returns an int
                    .sum();


            Map<MaintenanceStatus, Long> statusCounts = category.getAsset()
                    .stream()
                    .flatMap(asset -> asset.getMaintenanceRecords()
                            .stream())
                    .collect(Collectors.groupingBy(Maintenance::getStatus, Collectors.counting()));

            // Build the response map
            Map<String, Object> result = new HashMap<>();
            result.put("category", filter);
            result.put("currentCost", currentCost);
            result.put("downtime", downtime);
            result.put("statusCounts", statusCounts);

            return result;
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Map<String, Object> getAssetReport(Long assetId) {
        Asset asset = assetService.getAssetById(assetId);

        Map<String, Object> result = new HashMap<>();
        result.put("assetName", asset.getName());
        result.put("category", asset.getCategory().getName());
        result.put("lastDepreciationDate", asset.getDeps().getLast().getCalculationDate());
        result.put("depreciationAmount", asset.getDeps().getLast().getDepreciationAmount());

        List<Maintenance> scheduledMaintenance = asset.getMaintenanceRecords()
                .stream()
                .filter(maintenance -> maintenance.getStatus() == MaintenanceStatus.SCHEDULED)
                .toList();
        result.put("scheduledMaintenance", scheduledMaintenance);
        result.put("lastMaintenance", asset.getMaintenanceRecords().getLast());
        return result;
    }

    @NotNull
    private Map<String, Object> getMaintenanceReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Category> categories = categoryRepository.findAll();
        List<Map<String, Object>> categoryReports = new ArrayList<>();

        for (Category category : categories) {
            BigDecimal currentCost = category.getAsset().stream()
                    .map(Asset::getCurrentValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> categoryReport = new HashMap<>();
            categoryReport.put("category", category.getName());
            categoryReport.put("currentCost", currentCost);
            categoryReports.add(categoryReport);
        }

        // Build the final response map
        Map<String, Object> result = new HashMap<>();
        result.put("maintenanceReports", categoryReports);
        result.put("reportPeriod", Map.of("startDate", startDate, "endDate", endDate));

        return result;
    }

    @NotNull
    private Map<String, Object> getDepreciationReport() {
        List<Category> categories = categoryRepository.findAll();
        List<Map<String, Object>> categoryReports = new ArrayList<>();

        for (Category category : categories) {
            BigDecimal totalCurrentCost = category.getAsset()
                    .stream()
                    .flatMap(asset -> asset.getDeps()
                            .stream())
                    .map(DepreciationRecord::getCurrentValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalDepreciationAmount = category.getAsset()
                    .stream()
                    .flatMap(asset -> asset.getDeps()
                            .stream())
                    .map(DepreciationRecord::getDepreciationAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> categoryReport = new HashMap<>();
            categoryReport.put("category", category.getName());
            categoryReport.put("totalCurrentCost", totalCurrentCost);
            categoryReport.put("totalDepreciationAmount", totalDepreciationAmount);
            categoryReports.add(categoryReport);
        }
        // Build the final response map
        Map<String, Object> result = new HashMap<>();
        result.put("depreciationReports", categoryReports);

        return result;
    }
}
