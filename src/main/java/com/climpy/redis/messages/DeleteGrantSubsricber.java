package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.grant.Grant;
import com.climpy.redis.data.DataSubscriber;
import com.climpy.redis.gson.JsonChain;
import com.google.gson.JsonObject;

import java.util.UUID;

public class DeleteGrantSubsricber extends DataSubscriber {
    public DeleteGrantSubsricber() {
        super(RedrockPlugin.getInstance(), "delete_grant");
    }

    private UUID uuid;

    public JsonObject serialize() {
        return (new JsonChain()).addProperty("uuid", this.uuid.toString()).get();
    }

    @Override
    public void handleMessage(JsonObject jsonObject) {
        this.uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(uuid);
        if(profile == null) {
            return;
        }

        profile.saveProfile();
    }
}
