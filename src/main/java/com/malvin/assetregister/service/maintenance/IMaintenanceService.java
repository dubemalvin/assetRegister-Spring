package com.malvin.assetregister.service.maintenance;

import com.malvin.assetregister.dto.MaintenanceDto;
import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.Maintenance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IMaintenanceService {
    Maintenance scheduleMaintenance(Asset asset, Maintenance schedule);
    Maintenance performMaintenance(Maintenance request, Long maintenanceId);
    Maintenance maintenanceFinished(Maintenance request, Long maintenanceId);
    LocalDate calculateNextMaintenance(Asset asset);
    List<Maintenance> notifyMaintenanceDue();
    void cancelScheduledMaintenance(Maintenance request, Long maintenanceId);
    void setMaintenanceInterval(Asset asset, int months);
    BigDecimal getTotalMaintenanceCost(Long assetId);
    List<Maintenance> getMaintenanceHistory(Long assetId);
    List<Maintenance> getScheduledMaintenance();
    List<Maintenance> cancelledMaintenances();
    List<Maintenance> underMaintenance();

    MaintenanceDto convertToDto(Maintenance record);

    List<MaintenanceDto> convertToDtoList(List<Maintenance> records);
}
