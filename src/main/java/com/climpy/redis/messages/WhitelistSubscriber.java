package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.redis.data.DataSubscriber;
import com.climpy.server.provider.DefaultServerProvider;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class WhitelistSubscriber extends DataSubscriber {
    public WhitelistSubscriber() {
        super(RedrockPlugin.getInstance(), "whitelist");
    }

    public void handleMessage(JsonObject object) {
        String type = object.get("type").getAsString();
        String target = object.get("target").getAsString();
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        WhitelistType whitelistType = WhitelistType.getWhitelistType(type);
        if (whitelistType == WhitelistType.ADD) {
            UUID uuid = UUID.fromString(target);
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(uuid);
            RedrockPlugin.getInstance().getServer().getWhitelistedPlayers().add(targetPlayer);
            RedrockPlugin.getInstance().getServer().reloadWhitelist();
        } else if (whitelistType == WhitelistType.REMOVE) {
            UUID uuid = UUID.fromString(target);
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(uuid);
            RedrockPlugin.getInstance().getServer().getWhitelistedPlayers().remove(targetPlayer);
            RedrockPlugin.getInstance().getServer().reloadWhitelist();
        } else if (whitelistType == WhitelistType.ON) {
            if (target.equals(defaultServerProvider.getServerName()))
                Bukkit.getServer().setWhitelist(true);
        } else if (whitelistType == WhitelistType.OFF &&
                target.equals(defaultServerProvider.getServerName())) {
            Bukkit.getServer().setWhitelist(false);
        }
    }

    public enum WhitelistType {
        ADD("add"),
        REMOVE("remove"),
        ON("on"),
        OFF("off");

        WhitelistType(String internalName) {
            this.internalName = internalName;
        }

        private String internalName;

        public String getInternalName() {
            return this.internalName;
        }

        public static WhitelistType getWhitelistType(String whitelistType) {
            WhitelistType[] arrayOfWhitelistType;
            int i;
            byte b;
            for (arrayOfWhitelistType = values(), i = arrayOfWhitelistType.length, b = 0; b < i; ) {
                WhitelistType whitelistType1 = arrayOfWhitelistType[b];
                if (!whitelistType1.getInternalName().equalsIgnoreCase(whitelistType)) {
                    b++;
                    continue;
                }
                return whitelistType1;
            }
            return null;
        }
    }
}
