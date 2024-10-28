package com.malvin.assetregister.repository;

import com.malvin.assetregister.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {
}
