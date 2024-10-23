package com.malvin.assetregister.controller;

import com.malvin.assetregister.response.ApiResponse;
import com.malvin.assetregister.service.report.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("${api.prefix}/reports")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;
    @GetMapping("/report")
    public ResponseEntity<ApiResponse> generateReport(@RequestParam String reportType,
                                                      @RequestParam String filter,
                                                      @RequestParam LocalDateTime from,
                                                      @RequestParam LocalDateTime to){
        try {
            var report = reportService.generateReport(reportType,filter,from,to);
            return ResponseEntity.ok(new ApiResponse("success", report));
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
