package com.climpy.methods;

import com.climpy.RedrockPlugin;
import com.climpy.redis.data.DataPublisher;
import com.google.gson.JsonObject;

import java.util.UUID;

public class RedrockPacket {

    public static void sendReportObject(UUID reportedUUID, UUID reporterUUID, String serverName, String reason) {
        JsonObject reportObject = new JsonObject();
        reportObject.addProperty("reportedUUID", reportedUUID.toString());
        reportObject.addProperty("reporterUUID", reporterUUID.toString());
        reportObject.addProperty("serverName", serverName);
        reportObject.addProperty("message", reason);

        DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
        dataPublisher.write("report_alerts", reportObject);
    }

}
