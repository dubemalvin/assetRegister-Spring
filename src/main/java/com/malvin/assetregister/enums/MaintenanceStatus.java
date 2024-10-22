package com.malvin.assetregister.enums;

public enum MaintenanceStatus {

    SCHEDULED,            // Newly acquired asset
    SCHEDULE_CANCELLED,
    UNDER_MAINTENANCE, // Asset currently under maintenance
    OPERATIONAL,    // Fully functional asset after maintenance/repair
    REPAIRED,       // Asset that has been repaired and is now functional
    BEYOND_REPAIR  // Asset that cannot be repaired
}
