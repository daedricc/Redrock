package com.climpy.punishments;

import com.climpy.punishments.extnds.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum PunishmentType {
    BAN("Uzaklaştırma", "ban", Ban.class),
    IP_BAN("IP Uzaklaştırma", "ip_ban", Ban.class),
    KICK("Tekmeleme", "kick", Kick.class),
    MUTE("Susturma", "mute", Mute.class),
    IP_MUTE("IP Susturma", "ip_mute", Mute.class),
    WARN("Uyarı", "warn", Warn.class),
    BLACKLIST("Karaliste", "blacklist", Blacklist.class);

    private String displayName;
    private String internalName;
    private Class typeClass;

    public static PunishmentType getPunishmentType(String internalName) {
        for (PunishmentType punishmentType : PunishmentType.values()) {
            if(punishmentType.getInternalName().equals(internalName)) {
                return punishmentType;
            }
        }
        return null;
    }
}
