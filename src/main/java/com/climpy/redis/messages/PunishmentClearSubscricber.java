package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.punishments.PunishmentManager;
import com.climpy.redis.data.DataSubscriber;
import com.google.gson.JsonObject;

import java.util.UUID;

public class PunishmentClearSubscricber extends DataSubscriber {

    public PunishmentClearSubscricber() {
        super(RedrockPlugin.getInstance(), "punishment_clear");
    }

    @Override
    public void handleMessage(JsonObject paramJsonObject) {
        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        UUID uuid = UUID.fromString(paramJsonObject.get("uniqueUUID").getAsString());
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(uuid);
        if(profile == null) {
            return;
        }

        profile.getPunishments().clear();
        profile.saveProfile();
    }
}
