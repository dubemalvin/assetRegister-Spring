package com.malvin.assetregister.repository;

import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.Maintenance;
import com.malvin.assetregister.enums.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance,Long> {
    List<Maintenance> findByScheduledDateBetween(LocalDateTime scheduledDate, LocalDateTime scheduledDate2);

    List<Maintenance> findByAsset(Asset asset);

    List<Maintenance> findByStatus(MaintenanceStatus maintenanceStatus);
}
