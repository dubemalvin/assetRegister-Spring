package com.malvin.assetregister.controller;

import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.Maintenance;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.response.ApiResponse;
import com.malvin.assetregister.service.asset.IAssetService;
import com.malvin.assetregister.service.maintenance.IMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/maintenances")
public class MaintenanceController {
    private final IMaintenanceService maintenanceService;
    private final IAssetService assetService;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/schedule")
    public ResponseEntity<ApiResponse> scheduleMaintenance(@PathVariable Long id, @RequestBody Maintenance request){
        try {
            Asset asset = assetService.getAssetById(id);
            Maintenance maintenance = maintenanceService.scheduleMaintenance(asset,request);
            return ResponseEntity.ok(new ApiResponse("success", maintenance));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/perform")
    public ResponseEntity<ApiResponse> performMaintenance(@RequestBody Maintenance request, @PathVariable Long id){
        try {
            Maintenance maintenance = maintenanceService.performMaintenance(request, id);
            return ResponseEntity.ok(new ApiResponse("Success" ,maintenance));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/finished")
    public ResponseEntity<ApiResponse> maintenanceFinished(@RequestBody Maintenance request,@PathVariable Long id){
        try {
            Maintenance maintenance = maintenanceService.maintenanceFinished(request, id);
            return ResponseEntity.ok(new ApiResponse("Success" ,maintenance));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/notification")
    public ResponseEntity<ApiResponse> notifyMaintenanceDue(){
        try {
            List<Maintenance> maintenances=maintenanceService.notifyMaintenanceDue();
            return ResponseEntity.ok(new ApiResponse("Success", maintenances));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelScheduledMaintenance(@RequestBody Maintenance request, @PathVariable Long id){
        try {
            maintenanceService.cancelScheduledMaintenance(request, id);
            return ResponseEntity.ok(new ApiResponse("Success" ,null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/total-maintenance-cost")
    public ResponseEntity<ApiResponse> getTotalMaintenanceCost(@PathVariable Long id){
        try {
            BigDecimal cost = maintenanceService.getTotalMaintenanceCost(id);
            return ResponseEntity.ok(new ApiResponse("success",cost));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/maintenance-history")
    public ResponseEntity<ApiResponse> getMaintenanceHistory(@PathVariable Long id){
        try {
            List<Maintenance> maintenances=maintenanceService.getMaintenanceHistory(id);
            return ResponseEntity.ok(new ApiResponse("Success", maintenances));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/scheduled-maintenance")
    public ResponseEntity<ApiResponse> getScheduledMaintenance(){
        try {
            List<Maintenance> maintenances=maintenanceService.getScheduledMaintenance();
            return ResponseEntity.ok(new ApiResponse("Success", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/cancelled-maintenance")
    public ResponseEntity<ApiResponse> cancelledMaintenances(){
        try {
            List<Maintenance> maintenances=maintenanceService.cancelledMaintenances();
            return ResponseEntity.ok(new ApiResponse("Success", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/under-maintenance")
    public ResponseEntity<ApiResponse> underMaintenances(){
        try {
            List<Maintenance> maintenances=maintenanceService.underMaintenance();
            return ResponseEntity.ok(new ApiResponse("Success", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }

    }
}
