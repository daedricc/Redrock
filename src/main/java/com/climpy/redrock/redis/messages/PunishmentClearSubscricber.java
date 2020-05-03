package com.climpy.redrock.redis.messages;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataSubscriber;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.torch.punishments.PunishmentManager;
import com.google.gson.JsonObject;

import java.util.UUID;

public class PunishmentClearSubscricber extends DataSubscriber {

    public PunishmentClearSubscricber() {
        super(RedrockPlugin.getInstance(), "punishment_clear");
    }

    @Override
    public void handleMessage(JsonObject paramJsonObject) {
        PunishmentManager punishmentManager = TorchPlugin.getInstance().getPunishmentManager();
        UUID uuid = UUID.fromString(paramJsonObject.get("uniqueUUID").getAsString());
        Profile profile = TorchPlugin.getInstance().getProfileManager().getProfile(uuid);
        if (profile == null) {
            return;
        }

        //profile.getPunishments().clear();
        // profile.saveProfile();
    }
}
