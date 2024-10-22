package com.malvin.assetregister.controller;


import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.request.AddAssetReq;
import com.malvin.assetregister.request.UpdateAssetReq;
import com.malvin.assetregister.response.ApiResponse;
import com.malvin.assetregister.service.asset.IAssetService;
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
@RequestMapping("${api.prefix}/assets")
public class AssetController {
    private final IAssetService assetService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse>  addAsset(@RequestBody AddAssetReq request) {
        try {
            Asset asset = assetService.addAsset(request);
            return ResponseEntity.ok( new ApiResponse("Asset Added Successfully", asset));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Oops! error occurred", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/asset/{id}")
    public ResponseEntity<ApiResponse> getAssetById(@PathVariable Long id){
        try {
            Asset asset = assetService.getAssetById(id);
            return ResponseEntity.ok( new ApiResponse("Asset Successfully Retrieved", asset));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateAsset(@RequestBody UpdateAssetReq request,@PathVariable Long id){
        try {
            Asset asset = assetService.updateAsset(request,id);
            return ResponseEntity.ok( new ApiResponse("Asset Update Success", asset));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}/delete") //error
    public ResponseEntity<ApiResponse> deleteAsset(@PathVariable Long id){
        try {
            assetService.deleteAsset(id);
            return ResponseEntity.ok( new ApiResponse("Asset delete Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAssets(){
        try {
            List<Asset> assets = assetService.getAllAssets();
            return ResponseEntity.ok(new ApiResponse("Assets retrieved Successfully",assets));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Oops! error occurred", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse> getAssetsByCategory(@RequestParam String category){
        try {
            List<Asset> assets= assetService.getByCategory(category);
            return ResponseEntity.ok(new ApiResponse("Assets Retrieved Successfully", assets));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Oops!! Not Found", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse> getAssetsByName(@RequestParam String name){
        try {
            List<Asset> assets= assetService.getByName(name);
            return ResponseEntity.ok(new ApiResponse("Assets Retrieved Successfully", assets));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Oops!! Not Found", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/by-status")
    public ResponseEntity<ApiResponse> getAssetsByStatus(@RequestParam String status){
        try {
            List<Asset> assets= assetService.getByStatus(status);
            return ResponseEntity.ok(new ApiResponse("Assets Retrieved Successfully", assets));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Oops!! Not Found", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/by-category-and-status")
    public ResponseEntity<ApiResponse> getAssetsByCategoryAndStatus(@RequestParam String category,@RequestParam String status){
        try {
            List<Asset> assets= assetService.getByCategoryAndStatus(category, status);
            return ResponseEntity.ok(new ApiResponse("Assets Retrieved Successfully", assets));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Oops!! Not Found", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/by-value-range")
    public ResponseEntity<ApiResponse> getAssetsByInitialValueRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        try {
            List<Asset> assets= assetService.getByInitialValueRange(min, max);
            return ResponseEntity.ok(new ApiResponse("Assets Retrieved Successfully", assets));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Oops!! Not Found", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/assetCount")
    public ResponseEntity<ApiResponse> getAssetCount(){
        try {
            Integer count= assetService.countAssets();
            return ResponseEntity.ok(new ApiResponse("Assets Retrieved Successfully", count));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}




















