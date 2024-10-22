package com.malvin.assetregister.service.asset;

import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.enums.AssetStatus;
import com.malvin.assetregister.request.AddAssetReq;
import com.malvin.assetregister.request.UpdateAssetReq;

import java.math.BigDecimal;
import java.util.List;

public interface IAssetService {

    Asset addAsset(AddAssetReq request);
    Asset getAssetById(Long id);
    Asset updateAsset(UpdateAssetReq request, Long id);
    void deleteAsset(Long id);

    List<Asset> getAllAssets();
    List<Asset> getByCategory(String category);
    List<Asset> getByName(String name);
    List<Asset> getByStatus(String status);
    List<Asset> getByCategoryAndStatus(String category, String status);

    List<Asset> getByInitialValueRange(BigDecimal min, BigDecimal max);
    Integer countByCategoryName(String category);
    Integer countAssets();
}
