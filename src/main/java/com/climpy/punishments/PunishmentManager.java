package com.climpy.punishments;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.rank.RankType;
import com.climpy.redis.RedisConnection;
import com.climpy.redis.data.DataPublisher;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class PunishmentManager {

    public List<Punishment> getPunishments(UUID uniqueUUID, PunishmentType... punishmentTypes) {
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Profile profile = profileManager.getProfile(uniqueUUID);
        if (profile == null) {
            return new ArrayList<>();
        }

        List<Punishment> punishments = new ArrayList<>();
        for (PunishmentType punishmentType : punishmentTypes) {
            for (Punishment punishment : profile.getPunishments()) {
                if (punishment.getPunishmentType() == punishmentType) {
                    punishments.add(punishment);
                }
            }
        }

        return punishments;
    }

    public Map<UUID, List<Punishment>> getDatabasePunishments() {
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Map<UUID, List<Punishment>> punishments = new LinkedHashMap<>();

        for (Profile profile : profileManager.getDatabaseProfiles()) {
            List<Punishment> dataPunishments = profile.getPunishments();
            if (dataPunishments.isEmpty()) {
                continue;
            }

            punishments.put(profile.getUniqueUUID(), dataPunishments);
        }

        return punishments;
    }

    @SuppressWarnings("unchecked")
    public Punishment jsonPunishment(JsonObject jsonObject) {
        PunishmentType punishmentType = PunishmentType.getPunishmentType(jsonObject.get("punishmentType").getAsString());
        if (punishmentType == null) {
            return null;
        }

        try {
            return (Punishment) punishmentType.getTypeClass().getConstructor(JsonObject.class).newInstance(jsonObject);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Punishment getPunishment(UUID uniqueUUID) {
        List<Punishment> punishments = this.getPunishments(uniqueUUID, PunishmentType.values()).stream().filter(Punishment::isActive).collect(Collectors.toList());
        return punishments.isEmpty() ? null : punishments.get(0);
    }

    public Punishment getPunishment(UUID uniqueUUID, PunishmentType... punishmentType) {
        List<Punishment> punishments = this.getPunishments(uniqueUUID, punishmentType).stream().filter(Punishment::isActive).collect(Collectors.toList());
        return punishments.isEmpty() ? null : punishments.get(0);
    }

    public List<Punishment> getPunishments(UUID uniqueUUID) {
        List<Punishment> punishments = this.getPunishments(uniqueUUID, PunishmentType.values());
        return punishments.isEmpty() ? null : punishments;
    }

    public void applyPunishment(Punishment punishment, boolean silent, boolean announce) {
        RedisConnection redisConnection = RedrockPlugin.getInstance().getRedisConnection();
        DataPublisher dataPublisher = redisConnection.getDataPublisher();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("punishment", punishment.getJsonObject());
        jsonObject.addProperty("announce", announce);
        jsonObject.addProperty("silent", silent);
        dataPublisher.write("punishment", jsonObject);
    }
}
