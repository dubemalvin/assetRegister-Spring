package com.malvin.assetregister.service.report;


import java.time.LocalDateTime;
import java.util.Map;

public interface IReportService {


    Map<String, Object> generateReport(String reportType, String filter, LocalDateTime startDate, LocalDateTime endDate);
}


//{
//Report getCategoryReport(String name);
//Report getAssetReport(Long assetId);
//Report getAssetMaintenanceReport(Long assetId);
//Report getDepreciationReport(Long assetId);
//Report getMaintenanceReport(Long maintenanceId);
//
//        }