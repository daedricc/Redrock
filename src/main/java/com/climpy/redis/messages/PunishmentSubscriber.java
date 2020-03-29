package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.PunishmentType;
import com.climpy.punishments.event.PunishmentApplyEvent;
import com.climpy.redis.data.DataSubscriber;
import com.climpy.utils.MiscUtils;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class PunishmentSubscriber extends DataSubscriber {

    public PunishmentSubscriber() {
        super(RedrockPlugin.getInstance(), "punishment");
    }

    @Override
    public void handleMessage(JsonObject paramJsonObject) {
        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        Punishment punishment = punishmentManager.jsonPunishment(paramJsonObject.get("punishment").getAsJsonObject());

        boolean announce = paramJsonObject.get("announce").getAsBoolean();
        boolean silent = paramJsonObject.get("silent").getAsBoolean();

        List<Punishment> punishments = new ArrayList<>();
        punishments.add(punishment);

        if (punishment.isActive() && (punishment.getPunishmentType() == PunishmentType.IP_MUTE || punishment.getPunishmentType() == PunishmentType.IP_BAN || punishment.getPunishmentType() == PunishmentType.BLACKLIST)) {
            final ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
            Profile targetProfile = profileManager.getProfile(punishment.getUniqueUUID());

            targetProfile.getAlts().forEach(uniqueUUID -> {
                Punishment altPunishment = punishmentManager.jsonPunishment(paramJsonObject.get("punishment").getAsJsonObject());
                altPunishment.setUniqueUUID(uniqueUUID);
                punishments.add(altPunishment);
            });
        }

        PunishmentApplyEvent punishmentApplyEvent = new PunishmentApplyEvent(punishment, punishments, announce, silent);
        Bukkit.getPluginManager().callEvent(punishmentApplyEvent);
    }
}
