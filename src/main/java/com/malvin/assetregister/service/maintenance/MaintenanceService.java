package com.malvin.assetregister.service.maintenance;

import com.malvin.assetregister.dto.MaintenanceDto;
import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.Maintenance;
import com.malvin.assetregister.enums.MaintenanceStatus;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.repository.MaintenanceRepository;
import com.malvin.assetregister.service.asset.IAssetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public Maintenance scheduleMaintenance(Asset asset, Maintenance request) {
        Asset asset1 = assetService.getAssetById(asset.getId());
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
    public Maintenance performMaintenance(Maintenance request, Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance with this " + id + " was not scheduled"));

        if (maintenance.getPerformedDate() != null) {
            throw new IllegalStateException("Maintenance with ID " + id + " has already been performed.");
        }

        if (maintenance.getScheduledDate().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Maintenance with ID " + id + " cannot be performed before the scheduled date.");
        }

        maintenance.setPerformedDate(request.getPerformedDate());
        maintenance.setDetails(request.getDetails());
        maintenance.setStatus(MaintenanceStatus.UNDER_MAINTENANCE);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance maintenanceFinished(Maintenance request, Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance with this " + id + " was not scheduled"));

        // Check if the maintenance has been performed before marking it finished
        if (maintenance.getPerformedDate() == null) {
            throw new IllegalStateException("Maintenance with ID " + id + " must be performed before it can be finished.");
        }

        // Ensure maintenance finished is not set before it has been performed
        if (maintenance.getPerformedDate().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Maintenance with ID " + id + " cannot be marked as finished before it has been performed.");
        }

        maintenance.setDayOfCompletion(LocalDateTime.now());
        maintenance.setDetails(request.getDetails());
        maintenance.setCost(request.getCost());
        maintenance.setStatus(request.getStatus());
        maintenance.setDowntime(downtime(LocalDate.from(maintenance.getScheduledDate()), LocalDate.now().atStartOfDay()));
        return maintenanceRepository.save(maintenance);
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
        try {
            Asset asset1= assetService.getAssetById(id);
            return maintenanceRepository.findByAsset(asset1);
        } catch (Exception e) {
            throw new RuntimeException("error occurred");
        }
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

    @Override
    public MaintenanceDto convertToDto(Maintenance record){
        return modelMapper.map(record,MaintenanceDto.class);
    }

    @Override
    public List<MaintenanceDto> convertToDtoList(List<Maintenance> records){
        return records
                .stream()
                .map(this::convertToDto)
                .toList();
    }


}


