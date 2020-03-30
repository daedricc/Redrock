package com.climpy.profile;

import com.climpy.RedrockPlugin;
import com.climpy.mongo.CollectionManager;
import com.climpy.mongo.MongoConnection;
import com.climpy.mongo.RedrockMongoAPI;
import com.climpy.profile.grant.listener.GrantListener;
import com.climpy.profile.listener.ProfileListener;
import com.climpy.profile.rank.Rank;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Getter
public class ProfileManager {
    private RedrockPlugin plugin;
    public Map<UUID, Profile> profiles;
    private PluginManager pluginManager;

    public ProfileManager() {
        this.profiles = new HashMap<>();
    }

    public Profile getProfile(String name) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (offlinePlayer == null) {
            return null;
        }

        return this.getPlayerProfile(offlinePlayer.getUniqueId());
    }


    private Profile getPlayerProfile(UUID uniqueUUID) {
        return this.getPlayerProfile(uniqueUUID, null);
    }

    public Profile getProfile(UUID uniqueUUID) {
        return this.getPlayerProfile(uniqueUUID);
    }

    private Profile getPlayerProfile(UUID uniqueUUID, String name) {
        if (this.profiles.containsKey(uniqueUUID) && Bukkit.getPlayer(uniqueUUID) != null) {
            return this.profiles.get(uniqueUUID);
        } else {
            Document document = RedrockMongoAPI.find("user", Filters.eq("uniqueUUID", uniqueUUID.toString()));
            boolean create = true;

            if (document == null && (name == null || !name.isEmpty())) {
                Document document2 = RedrockMongoAPI.find("user", Filters.eq("name", name));
                if (document2 != null) {
                    document2.put("uniqueUUID", uniqueUUID);
                    document = document2;
                } else {
                    create = false;
                }
            }

            if (create) {
                this.profiles.put(uniqueUUID, new Profile(document));
                return this.profiles.get(uniqueUUID);
            }

            return null;
        }
    }

    public Profile getDatabaseProfile(UUID uniqueUUID) {
        Document document = RedrockMongoAPI.find("user", Filters.eq("uniqueUUID", uniqueUUID.toString()));
        return document == null ? null : new Profile(document);
    }

    public Profile getDatabaseProfile(String name) {
        Document document = RedrockMongoAPI.find("user", Filters.eq("name", name));
        return document == null ? null : new Profile(document);
    }


    public List<Profile> getDatabaseProfiles() {
        List<Profile> profiles = new LinkedList<>();

        try (MongoCursor mongoCursor = RedrockMongoAPI.findAll("user").iterator()) {
            while (mongoCursor.hasNext()) {
                profiles.add(new Profile((Document) mongoCursor.next()));
            }
        }

        return profiles;
    }

    public List<Profile> getDatabaseIPProfile(String ipAddress) {
        List<Profile> profiles = new LinkedList<>();

        MongoConnection mongoConnection = RedrockPlugin.getInstance().getMongoConnection();
        CollectionManager collectionManager = mongoConnection.getCollectionManager();

        MongoCollection mongoCollection = collectionManager.getMongoCollection("user");
        try (MongoCursor mongoCursor = mongoCollection.find(Filters.eq("currentAddress", ipAddress)).iterator()) {
            while (mongoCursor.hasNext()) {
                profiles.add(new Profile((Document) mongoCursor.next()));
            }
        }

        return profiles;
    }
}
