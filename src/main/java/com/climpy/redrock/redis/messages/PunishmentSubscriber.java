package com.climpy.redrock.redis.messages;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataSubscriber;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.torch.profile.ProfileManager;
import com.climpy.torch.punishments.Punishment;
import com.climpy.torch.punishments.PunishmentManager;
import com.climpy.torch.punishments.PunishmentType;
import com.climpy.torch.punishments.event.PunishmentApplyEvent;
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
        PunishmentManager punishmentManager = TorchPlugin.getInstance().getPunishmentManager();
        Punishment punishment = punishmentManager.jsonPunishment(paramJsonObject.get("punishment").getAsJsonObject());

        boolean announce = paramJsonObject.get("announce").getAsBoolean();
        boolean silent = paramJsonObject.get("silent").getAsBoolean();

        List<Punishment> punishments = new ArrayList<>();
        punishments.add(punishment);

        if (punishment.isActive() && (punishment.getPunishmentType() == PunishmentType.IP_MUTE || punishment.getPunishmentType() == PunishmentType.IP_BAN || punishment.getPunishmentType() == PunishmentType.BLACKLIST)) {
            final ProfileManager profileManager = TorchPlugin.getInstance().getProfileManager();
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
