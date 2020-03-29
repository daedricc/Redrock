package com.climpy.punishments;

import com.climpy.punishments.event.PunishmentExpireEvent;
import com.climpy.utils.MiscUtils;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;

@Setter
@Getter
@Data
public abstract class Punishment implements Cloneable {
    protected UUID uniqueUUID;
    protected UUID addedBy;
    protected UUID removedBy;
    protected String punishmentID;
    protected String ipAddress;
    protected String addedReason;
    protected String removedReason;
    protected PunishmentType punishmentType;
    protected boolean active = true;

    protected long duration = Long.MAX_VALUE;
    protected long date = System.currentTimeMillis();

    public Punishment(PunishmentType punishmentType, String ipAddress, UUID uniqueUUID, UUID addedBy, String addedReason) {
        this.punishmentID = MiscUtils.generatePunishmentRandomID();
        this.punishmentType = punishmentType;
        this.ipAddress = ipAddress;
        this.uniqueUUID = uniqueUUID;
        this.addedBy = addedBy;
        this.addedReason = addedReason;
    }

    public Punishment(PunishmentType punishmentType, String ipAddress, UUID uniqueUUID, UUID addedBy, String addedReason, long duration) {
        this.punishmentID = MiscUtils.generatePunishmentRandomID();
        this.punishmentType = punishmentType;
        this.ipAddress = ipAddress;
        this.uniqueUUID = uniqueUUID;
        this.addedBy = addedBy;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public Punishment(JsonObject jsonObject) {
        this.punishmentID = jsonObject.get("punishmentID").getAsString();
        this.punishmentType = PunishmentType.getPunishmentType(jsonObject.get("punishmentType").getAsString());
        this.ipAddress = jsonObject.get("ipAddress").getAsString();
        this.uniqueUUID = UUID.fromString(jsonObject.get("uniqueUUID").getAsString());
        if (jsonObject.has("addedBy")) {
            this.addedBy = UUID.fromString(jsonObject.get("addedBy").getAsString());
        }
        this.addedReason = jsonObject.get("addedReason").getAsString();
        this.date = jsonObject.get("date").getAsLong();
        this.duration = jsonObject.get("duration").getAsLong();
        this.active = jsonObject.get("active").getAsBoolean();
        if (!jsonObject.get("active").getAsBoolean()) {
            if (jsonObject.has("removedBy")) {
                this.removedBy = UUID.fromString(jsonObject.get("removedBy").getAsString());
            }
            this.removedReason = jsonObject.get("removedReason").getAsString();
        }
    }

    public JsonObject getJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("punishmentID", this.punishmentID);
        jsonObject.addProperty("punishmentType", this.punishmentType.getInternalName());
        jsonObject.addProperty("ipAddress", this.ipAddress);
        jsonObject.addProperty("uniqueUUID", this.uniqueUUID.toString());
        if (this.addedBy != null) {
            jsonObject.addProperty("addedBy", this.addedBy.toString());
        }
        jsonObject.addProperty("addedReason", this.addedReason);
        jsonObject.addProperty("date", this.date);
        jsonObject.addProperty("duration", this.duration);
        jsonObject.addProperty("active", this.active);
        if (!this.active) {
            if (this.removedBy != null) {
                jsonObject.addProperty("removedBy", this.removedBy.toString());
            }
            jsonObject.addProperty("removedReason", this.removedReason);
        }
        return jsonObject;
    }

    private boolean checkPunishment() {
        if (this.active && !this.isPermanent() && this.isExpired()) {
            PunishmentExpireEvent punishmentExpireEvent = new PunishmentExpireEvent(this);
            Bukkit.getPluginManager().callEvent(punishmentExpireEvent);

            return punishmentExpireEvent.isCancelled();
        }
        return this.active;
    }

    public boolean isActive() {
        return this.checkPunishment();
    }

    public boolean isPermanent() {
        return this.duration == Long.MAX_VALUE;
    }

    public boolean isExpired() {
        return (!this.isPermanent()) && (this.duration <= System.currentTimeMillis());
    }

    public abstract String getWarningMessage(boolean active, boolean silent);

    public abstract StringBuilder getPunishmentMessage();
}
