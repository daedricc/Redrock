package com.climpy.report;

import com.climpy.RedrockMongoAPI;
import com.climpy.utils.MiscUtils;
import com.mongodb.client.model.Filters;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter @Setter @Data
public class ReportData {

    private String reportID;
    private UUID uniqueUUID;
    private UUID addedBy;
    private String reason;
    private String server;
    private long date = System.currentTimeMillis();
    private ReportStatus reportStatus = ReportStatus.ACTIVE;

    public ReportData(UUID uniqueUUID, UUID addedBy, String reason, String server) {
        this.reportID = MiscUtils.generateReportRandomID();
        this.uniqueUUID = uniqueUUID;
        this.addedBy = addedBy;
        this.reason = reason;
        this.server = server;
    }

    public ReportData(Document document) {
        this.reportID = document.getString("reportID");
        this.uniqueUUID = UUID.fromString(document.getString("uniqueUUID"));
        this.addedBy = UUID.fromString(document.getString("addedBy"));
        this.reason = document.getString("reason");
        this.server = document.getString("server");
        this.date = document.getLong("date");
        this.reportStatus = ReportStatus.getReportStatus(document.getString("reportStatus"));
    }

    public void saveReport() {
        Document document = new Document();

        document.put("reportID", this.reportID);
        document.put("uniqueUUID", this.uniqueUUID.toString());
        document.put("addedBy", this.addedBy.toString());
        document.put("reason", this.reason);
        document.put("server", this.server);
        document.put("date", this.date);
        document.put("reportStatus", this.reportStatus.getInternalName());

        RedrockMongoAPI.replaceOne("report", Filters.eq("reportID", this.reportID), document, true);
    }

}
