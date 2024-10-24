package com.malvin.assetregister.repository;

import com.malvin.assetregister.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByAssetId(Long id);
}
