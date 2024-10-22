package com.malvin.assetregister.service.asset;

import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.Category;
import com.malvin.assetregister.enums.DepreciationMethod;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.repository.AssetRepository;
import com.malvin.assetregister.repository.CategoryRepository;
import com.malvin.assetregister.request.AddAssetReq;
import com.malvin.assetregister.request.UpdateAssetReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService implements IAssetService {
    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Asset addAsset(AddAssetReq request) {

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory()))
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory());

                    newCategory.setDepreciationMethod(DepreciationMethod.SLD);
                    return categoryRepository.save(newCategory);
                });

        request.setCategory(String.valueOf(category));
        return assetRepository.save(createAsset(request, category));
    }

    private Asset createAsset(AddAssetReq request, Category category) {
        return  new Asset(
                request.getName(),
                request.getDescription(),
                request.getPurchaseDate(),
                request.getInitialValue(),
                request.getInitialValue(),
                request.getDepRate(),
                request.getStatus(),
                category
        );
    }

    @Override
    public Asset getAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Oops!! asset not found"));
    }

    @Override
    public Asset updateAsset(UpdateAssetReq request, Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oops!! asset not found"));

        asset.setName(request.getName());
        asset.setDescription(request.getDescription());
        asset.setInitialValue(request.getInitialValue());
        asset.setDepRate(request.getDepRate());
        asset.setPurchaseDate(request.getPurchaseDate());

        return assetRepository.save(asset);
    }

    @Override
    public void deleteAsset(Long id) {
        assetRepository.findById(id).ifPresentOrElse(assetRepository::delete,
        ()-> {
            throw new ResourceNotFoundException("Oops!! asset not found, for deletion");
        });
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public List<Asset> getByCategory(String category) {
        return assetRepository
                .findAll()
                .stream()
                .filter(asset ->
                        asset.getCategory().getName().equalsIgnoreCase(category))
                .toList();
    }

    @Override
    public List<Asset> getByName(String name) {
        return assetRepository
                .findAll()
                .stream()
                .filter(asset -> asset.getName().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public List<Asset> getByStatus(String status) {
        return getAllAssets()
                .stream()
                .filter(asset->
                        asset.getStatus().name().equals(status))
                .toList();
    }

    @Override
    public List<Asset> getByCategoryAndStatus(String category, String status) {
        return getAllAssets()
                .stream()
                .filter(asset -> category == null || asset.getCategory().getName().equalsIgnoreCase(category))
                .filter(asset -> status == null || asset.getStatus().name().equalsIgnoreCase(status))
                .toList();
    }


    @Override
    public List<Asset> getByInitialValueRange(BigDecimal min, BigDecimal max) {
        return assetRepository.findByInitialValueBetween(min,max);
    }

    @Override
    public Integer countByCategoryName(String category) {
        return assetRepository.countByCategoryName(category);
    }

    @Override
    public Integer countAssets() {
        return Math.toIntExact(assetRepository.count());
    }
}
