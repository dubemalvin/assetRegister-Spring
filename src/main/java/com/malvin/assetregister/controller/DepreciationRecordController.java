package com.malvin.assetregister.controller;

import com.malvin.assetregister.entity.DepreciationRecord;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.response.ApiResponse;
import com.malvin.assetregister.service.depreciation.IDepreciationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/depreciation-records")
public class DepreciationRecordController {
    private final IDepreciationService depreciationService;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{assetId}/records")
    public ResponseEntity<ApiResponse> getDepreciationRecordsUsingAssetId(@PathVariable Long assetId){
        try {
            List<DepreciationRecord> records = depreciationService.findAllByAssetId(assetId);
            return ResponseEntity.ok(new ApiResponse("Records Retrieved Successfully", records));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllDepreciationRecords(){
        try {
            List<DepreciationRecord> records = depreciationService.findAll();
            return ResponseEntity.ok(new ApiResponse("Records Retrieved Successfully", records));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/by-date-range")
    public ResponseEntity<ApiResponse> getRecordsUsingDateRange(@RequestParam LocalDateTime from , @RequestParam LocalDateTime to){
        try {
            List<DepreciationRecord> records= depreciationService.findDepreciationRecordsByDateRange(from, to);
            return ResponseEntity.ok(new ApiResponse("Records Retrieved successfully",records));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{assetId}/by-date-range")
    public ResponseEntity<ApiResponse> getRecordsUsingAssetAndDateRange(@PathVariable Long assetId, @RequestParam LocalDateTime from, @RequestParam LocalDateTime to){
        try {
            List<DepreciationRecord> records = depreciationService.findDepreciationRecordsByAssetAndDateRange(assetId,from,to);
            return ResponseEntity.ok(new ApiResponse("Records Retrieved Successfully", records));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
