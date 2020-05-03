package com.climpy.redrock.redis.messages;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataSubscriber;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageSubscriber extends DataSubscriber {
    public MessageSubscriber() {
        super(RedrockPlugin.getInstance(), "message");
    }

    public void handleMessage(JsonObject object) {
        MessageType type = MessageType.getMessageType(object.get("type").getAsString());
        String message = object.get("message").getAsString();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile profile = TorchPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
            boolean toMessage = false;
            if (type == MessageType.RANK) {
                if (!profile.toPlayer().hasPermission("redrock.moderator"))
                    continue;
                toMessage = true;
            } else if (type == MessageType.PLAYER) {
                UUID uniqueUUID = UUID.fromString(object.get("value").getAsString());
                if (!profile.getUniqueUUID().equals(uniqueUUID))
                    continue;
                toMessage = true;
            }
            if (toMessage)
                onlinePlayer.sendMessage(colorized(message));
        }
    }

    private String colorized(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public enum MessageType {
        PLAYER("player"),
        RANK("rank"),
        UNKNOWN("unknown");

        MessageType(String internalName) {
            this.internalName = internalName;
        }

        private String internalName;

        public String getInternalName() {
            return this.internalName;
        }

        public static MessageType getMessageType(String messageType) {
            MessageType[] arrayOfMessageType;
            int i;
            byte b;
            for (arrayOfMessageType = values(), i = arrayOfMessageType.length, b = 0; b < i; ) {
                MessageType eventType = arrayOfMessageType[b];
                if (!eventType.getInternalName().equalsIgnoreCase(messageType.toLowerCase())) {
                    b++;
                    continue;
                }
                return eventType;
            }
            return UNKNOWN;
        }
    }
}
