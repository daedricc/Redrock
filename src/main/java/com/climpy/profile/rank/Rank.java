package com.climpy.profile.rank;

import com.climpy.RedrockPlugin;
import com.climpy.mongo.RedrockMongoAPI;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.utils.C;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter @Data
public class Rank {
    private static MongoCollection<Document> collection;
    private static Map<UUID, Rank> ranks = new HashMap<>();

    private UUID uniqueUUID;

    private String displayName, prefix;
    private List<String> permissions;

    private int weight;

    private ChatColor color;

    private boolean defaultRank;

    private List<Rank> inherited;

    public Rank(String displayName) {
        this.prefix = "";
        this.color = ChatColor.WHITE;
        this.permissions = new ArrayList<>();
        this.inherited = new ArrayList<>();
        this.uniqueUUID = UUID.randomUUID();
        this.displayName = displayName;
        ranks.put(this.uniqueUUID, this);
    }

    public Rank(UUID uuid, String displayName) {
        this.prefix = "";
        this.color = ChatColor.WHITE;
        this.permissions = new ArrayList<>();
        this.inherited = new ArrayList<>();
        this.uniqueUUID = uuid;
        this.displayName = displayName;
    }

    public Rank(UUID uniqueUUID, String displayName, String prefix, int weight, ChatColor color, boolean defaultRank) {
        this.prefix = "";
        this.color = ChatColor.WHITE;
        this.permissions = new ArrayList<>();
        this.inherited = new ArrayList<>();
        this.uniqueUUID = uniqueUUID;
        this.displayName = displayName;
        this.prefix = prefix;
        this.color = color;
        this.weight = weight;
        this.defaultRank = defaultRank;
        ranks.put(uniqueUUID, this);
    }

    public void load() {
        load((Document)collection.find(Filters.eq("uuid", this.uniqueUUID.toString())).first());
    }

    private void load(Document document) {
        if (document == null)
            return;
        this.prefix = ChatColor.translateAlternateColorCodes('&', document.getString("prefix"));
        this.color = ChatColor.valueOf(document.getString("color"));
        this.weight = document.getInteger("weight").intValue();
        this.defaultRank = document.getBoolean("defaultRank").booleanValue();
    }

    private void saveRanks() {
        Document document = new Document();

        document.put("uuid", this.uniqueUUID.toString());
        document.put("displayName", this.displayName);
        document.put("prefix", this.prefix.replace(String.valueOf('§'), "&"));
        document.put("color", this.color.name());
        document.put("weight", Integer.valueOf(this.weight));
        document.put("defaultRank", Boolean.valueOf(this.defaultRank));
        document.put("permissions", new Gson().toJson(this.permissions));
        document.put("inherits", new Gson().toJson(this.inherited.stream().map(Rank::getUniqueUUID).map(UUID::toString).collect(Collectors.toList())));

        RedrockMongoAPI.replaceOne("ranks", Filters.eq("uuid", this.uniqueUUID.toString()), document, true);
       //TODO: RANKLAR KAYDEDILDI MESAJI
    }

    public boolean addPermission(String permission) {
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
            return true;
        }
        return false;
    }

    public boolean deletePermission(String permission) {
        return this.permissions.remove(permission);
    }

    public boolean hasPermission(String permission) {
        if (this.permissions.contains(permission))
            return true;
        for (Rank rank : this.inherited) {
            if (rank.hasPermission(permission))
                return true;
        }
        return false;
    }

    public boolean canInherit(Rank rankToCheck) {
        if (this.inherited.contains(rankToCheck) || rankToCheck.inherited.contains(this))
            return false;
        for (Rank rank : this.inherited) {
            if (!rank.canInherit(rankToCheck))
                return false;
        }
        return true;
    }

    public List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.addAll(this.permissions);
        for (Rank rank : this.inherited)
            permissions.addAll(rank.getAllPermissions());
        return permissions;
    }

    public void delete() {
        ranks.remove(this.uniqueUUID);
        collection.deleteOne(Filters.eq("uuid", this.uniqueUUID.toString()));
        //TODO: Silinen rank broadcast
    }

    public String getColoredName() {
        return C.translate(this.color + this.displayName);
    }

    public static Rank getRankByUuid(UUID uuid) {
        return ranks.get(uuid);
    }

    public static Rank getRankByDisplayName(String name) {
        for (Rank rank : ranks.values()) {
            if (rank.getDisplayName().equalsIgnoreCase(name))
                return rank;
        }
        return null;
    }

    public boolean isDefaultRank() {
        return this.defaultRank;
    }

    public static Rank getDefaultRank() {
        for (Rank rank : ranks.values()) {
            if (rank.isDefaultRank()) {
                return rank;
            }
        }

        Rank defaultRank = new Rank("Default");
        defaultRank.setDefaultRank(true);
        defaultRank.saveRanks();

        ranks.put(defaultRank.getUniqueUUID(), defaultRank);

        return defaultRank;
    }


    public void setColor(ChatColor color) {
        this.color = color;
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = profileManager.getProfile(player.getUniqueId());
            //   if (profile.getActiveRank().equals(this))
            //       profile.refreshDisplayName();
        }
    }

    public int compareTo(Rank other) { return Integer.compare(this.weight, other.weight); }

    public static void init() {
        collection = RedrockPlugin.getInstance().getMongoConnection().getCollectionManager().getMongoCollection("ranks");
        Map<Rank, List<UUID>> inheritanceReferences = new HashMap<>();
        Exception exception1 = null, exception2 = null;
        try {
            MongoCursor<Document> cursor = collection.find().iterator();
            try {
                while (cursor.hasNext()) {
                    Document document = (Document)cursor.next();
                    Rank rank = new Rank(UUID.fromString(document.getString("uuid")), document.getString("displayName"));
                    rank.load(document);
                    ((List) new Gson().fromJson(document.getString("permissions"), new TypeToken<List<UUID>>() {
                    }.getType())).forEach(perm -> {

                    });
                    List<UUID> ranksToInherit = new ArrayList<>();
                    for (JsonElement element : (new JsonParser()).parse(document.getString("inherits")).getAsJsonArray())
                        ranksToInherit.add(UUID.fromString(element.getAsString()));
                    inheritanceReferences.put(rank, ranksToInherit);
                    ranks.put(rank.getUniqueUUID(), rank);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } finally {
            exception2 = null;
            if (exception1 == null) {
                exception1 = exception2;
            } else if (exception1 != exception2) {
                exception1.addSuppressed(exception2);
            }
        }


        getDefaultRank();
    }
}
