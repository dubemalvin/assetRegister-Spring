package com.malvin.assetregister.service.maintenance;

import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.Maintenance;
import com.malvin.assetregister.enums.MaintenanceStatus;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.repository.MaintenanceRepository;
import com.malvin.assetregister.service.asset.IAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService implements IMaintenanceService  {
    private final MaintenanceRepository maintenanceRepository;
    private final IAssetService assetService;

    @Override
    public Maintenance scheduleMaintenance(Asset asset, Maintenance request) {
        Asset asset1= assetService.getAssetById(asset.getId());
        Maintenance maintenance = new Maintenance();
        maintenance.setAsset(asset1);
        maintenance.setToday(LocalDateTime.now());
        maintenance.setScheduledDate(request.getScheduledDate());
        maintenance.setDetails(request.getDetails());
        maintenance.setCost(request.getCost());
        maintenance.setStatus(MaintenanceStatus.SCHEDULED);
        maintenance.setType(request.getType());
       return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance performMaintenance(Maintenance request,Long id) {
        Maintenance asset =maintenanceRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Maintenance with this "+id+" was not scheduled"));
        asset.setPerformedDate(request.getPerformedDate());
        asset.setDetails(request.getDetails());
        asset.setStatus(MaintenanceStatus.UNDER_MAINTENANCE);
        return maintenanceRepository.save(asset);
    }

    @Override
    public Maintenance maintenanceFinished(Maintenance request,Long id) {
        Maintenance asset =maintenanceRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Maintenance with this "+id+" was not scheduled"));
        asset.setDayOfCompletion(LocalDateTime.now());
        asset.setDetails(request.getDetails());
        asset.setCost(request.getCost());
        asset.setStatus(request.getStatus());
        asset.setDowntime(downtime(LocalDate.from(asset.getScheduledDate()), LocalDate.now().atStartOfDay()));
        return maintenanceRepository.save(asset);
    }

    @Override
    public LocalDate calculateNextMaintenance(Asset asset) {
        return null;
    }

    private int downtime(LocalDate scheduledDate, LocalDateTime now) {
        LocalDateTime scheduledDateTime = scheduledDate.atStartOfDay();
        return (int) ChronoUnit.MINUTES.between(scheduledDateTime, now);
    }

    @Override
    public List<Maintenance> notifyMaintenanceDue() {
        return maintenanceRepository.findByScheduledDateBetween(
                LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(7).atStartOfDay()
        );
    }

    @Override
    public void cancelScheduledMaintenance(Maintenance request, Long id) {
        maintenanceRepository.findById(id).ifPresentOrElse(reason->{
            Maintenance maintenance = new Maintenance();
            maintenance.setDetails(request.getDetails());
            maintenance.setStatus(MaintenanceStatus.SCHEDULE_CANCELLED);
            maintenanceRepository.save(maintenance);
            },
                ()->{
            throw  new ResourceNotFoundException("Maintenance with id not found");
        });
    }

    @Override
    public void setMaintenanceInterval(Asset asset, int months) {
        //TODO
    }

    @Override
    @Transactional
    public BigDecimal getTotalMaintenanceCost(Long assetId) {
        List<Maintenance> records = getMaintenanceHistory(assetId);

        return records.stream()
                .filter(record ->
                        record.getStatus() == MaintenanceStatus.OPERATIONAL ||
                                record.getStatus() == MaintenanceStatus.REPAIRED)
                .map(Maintenance::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Maintenance> getMaintenanceHistory(Long id) {
        Asset asset1= assetService.getAssetById(id);
        return maintenanceRepository.findByAsset(asset1);
    }

    @Override
    public List<Maintenance> getScheduledMaintenance() {
        return maintenanceRepository.findByStatus(MaintenanceStatus.SCHEDULED);
    }

    @Override
    public List<Maintenance> cancelledMaintenances() {
        return maintenanceRepository.findByStatus(MaintenanceStatus.SCHEDULE_CANCELLED);
    }

    @Override
    public List<Maintenance> underMaintenance() {
        return maintenanceRepository.findByStatus(MaintenanceStatus.UNDER_MAINTENANCE);
    }


}


