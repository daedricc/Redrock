package com.climpy.redrock.report;

import com.climpy.torch.mongo.MongoAPI;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class ReportManager {
    public ReportData getReport(String reportID) {
        return this.getDatabaseReports().stream().filter(reportData -> reportData.getReportID().equals(reportID)).findFirst().orElse(null);
    }

    public List<ReportData> getReports(UUID reportedUUID) {
        return this.getDatabaseReports().stream().filter(reportData -> reportData.getUniqueUUID().equals(reportedUUID)).collect(Collectors.toList());
    }

    public List<ReportData> getReporters(UUID reporterUUID) {
        return this.getDatabaseReports().stream().filter(reportData -> reportData.getAddedBy().equals(reporterUUID)).collect(Collectors.toList());
    }

    public List<ReportData> getReports(ReportStatus reportStatus) {
        return this.getDatabaseReports().stream().filter(reportData -> reportData.getReportStatus() == reportStatus).collect(Collectors.toList());
    }

    public List<ReportData> getReports(UUID uniqueUUID, ReportStatus... reportStatus) {
        return this.getReports(reportStatus).stream().filter(reportData -> {
            return reportData.getUniqueUUID().equals(uniqueUUID);
        }).collect(Collectors.toList());
    }

    public List<ReportData> getReports(ReportStatus... reportStatus) {
        List<ReportData> reports = new ArrayList<>();

        for (ReportData reportData : this.getReports()) {
            boolean checked = false;

            for (ReportStatus statusReport : reportStatus) {
                if (reportData.getReportStatus() == statusReport) {
                    checked = true;
                }
            }

            if (!checked) {
                continue;
            }

            reports.add(reportData);
        }

        return reports;
    }

    public List<ReportData> getDatabaseReports() {
        List<ReportData> reports = new LinkedList<>();

        try (MongoCursor mongoCursor = MongoAPI.findAll("report").iterator()) {
            while (mongoCursor.hasNext()) {
                reports.add(new ReportData((Document) mongoCursor.next()));
            }
        }

        return reports;
    }
}
