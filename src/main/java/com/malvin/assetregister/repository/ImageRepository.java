package com.malvin.assetregister.repository;

import com.malvin.assetregister.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
