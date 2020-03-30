package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.grant.Grant;
import com.climpy.redis.data.DataSubscriber;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddGrantSubsricber extends DataSubscriber {
    public AddGrantSubsricber() {
        super(RedrockPlugin.getInstance(), "add_grant");
    }

    @Override
    public void handleMessage(JsonObject jsonObject) {

    }
}
