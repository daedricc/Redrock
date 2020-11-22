package com.climpy.redrock.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportStatus {
    ACTIVE("Aktif", "active"),
    PROCESS("İşlemde", "process"),
    INACTIVE("Kapalı", "inactive");

    private String displayName;
    private String internalName;

    public static ReportStatus getReportStatus(String internalName) {
        for (ReportStatus reportStatus : ReportStatus.values()) {
            if (reportStatus.getInternalName().equals(internalName)) {
                return reportStatus;
            }
        }
        return ReportStatus.INACTIVE;
    }
}
