package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.redis.data.DataSubscriber;
import com.climpy.utils.C;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StreamingSubscriber extends DataSubscriber {
    public StreamingSubscriber() {
        super(RedrockPlugin.getInstance(), "streaming");
    }

    @Override
    public void handleMessage(JsonObject paramJsonObject) {
        UUID uuid = UUID.fromString(paramJsonObject.get("uuid").getAsString());
        String streamLink = paramJsonObject.get("stream_link").getAsString();

        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(uuid);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(" ");
            onlinePlayer.sendMessage(C.GOLD + " » " + profile.getActiveRank().getColor() + profile.getName() + C.YELLOW + " şu anda akış halinde! " + C.GRAY + "(" + streamLink + ")");
            onlinePlayer.sendMessage(" ");
        }
    }
}
