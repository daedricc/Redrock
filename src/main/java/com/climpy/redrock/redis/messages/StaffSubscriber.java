package com.climpy.redrock.redis.messages;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataSubscriber;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.redrock.utils.fanciful.FancyMessage;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StaffSubscriber extends DataSubscriber {
    public StaffSubscriber() {
        super(RedrockPlugin.getInstance(), "staff_join_leave");
    }

    public void handleMessage(JsonObject object) {
        StaffEventType staffEventType = StaffEventType.getEventType(object.get("type").getAsString());
        UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        String serverName = object.get("serverName").getAsString();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile onlineUser = TorchPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
            if (onlineUser.toPlayer().hasPermission("redrock.moderator"))
                setMessageFormat(staffEventType, serverName, uuid).send(onlinePlayer);
        }
    }

    private FancyMessage setMessageFormat(StaffEventType staffEventType, String serverName, UUID uuid) {
        FancyMessage fancyMessage = new FancyMessage();
        Profile eventUser = TorchPlugin.getInstance().getProfileManager().getProfile(uuid);
        fancyMessage.text(ChatColor.translateAlternateColorCodes('&', ChatColor.BLUE + "[Personel] " + ChatColor.AQUA + "(" + serverName + ") " + eventUser
                .getName() + " " + staffEventType.getDescription()));


     /*   SymbolType symbolType = eventUser.getSymbolType();
        fancyMessage.tooltip(ChatColor.GRAY + "Rütbe: " + eventUser.getActiveRank().getColor() + eventUser.getActiveRank().getDisplayName() +
                "\n" + ChatColor.GRAY + "Sembol Türü: " + ((symbolType == SymbolType.SYMBOL_0) ? (ChatColor.GRAY + "Kullanılmıyor.") : (symbolType
                .getPrefix() + ChatColor.GRAY + " (" + symbolType.getDisplayName() + ")"))); */
        return fancyMessage;
    }

    public enum StaffEventType {
        JOIN("sunucuya giriş yaptı.", "join"),
        QUIT("sunucudan çıkış yaptı", "quit"),
        UNKNOWN("durum bulunamadı.", "unknown");

        StaffEventType(String description, String internalName) {
            this.description = description;
            this.internalName = internalName;
        }

        @Getter
        private String description;

        private String internalName;

        public String getInternalName() {
            return this.internalName;
        }

        public static StaffEventType getEventType(String eventState) {
            StaffEventType[] arrayOfStaffEventType;
            int i;
            byte b;
            for (arrayOfStaffEventType = values(), i = arrayOfStaffEventType.length, b = 0; b < i; ) {
                StaffEventType staffEventType = arrayOfStaffEventType[b];
                if (!staffEventType.getInternalName().equalsIgnoreCase(eventState.toLowerCase())) {
                    b++;
                    continue;
                }
                return staffEventType;
            }
            return UNKNOWN;
        }
    }
}
