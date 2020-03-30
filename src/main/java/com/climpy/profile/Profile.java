package com.climpy.profile;

import com.climpy.RedrockPlugin;
import com.climpy.mongo.RedrockMongoAPI;
import com.climpy.profile.grant.Grant;
import com.climpy.profile.grant.event.GrantAppliedEvent;
import com.climpy.profile.grant.event.GrantExpireEvent;
import com.climpy.profile.rank.Rank;
import com.climpy.profile.symbol.SymbolType;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Data @Getter @Setter
public class Profile {
    private UUID uniqueUUID;
    private String name;

    private SymbolType symbolType = SymbolType.SYMBOL_0;

    private String currentAddress = "127.0.0.1";
    private List<String> ipAddresses = new ArrayList<>();

    private boolean staffMode = false, staffChat = false, vanishStatus = false, frozenStatus = false, messagesVisible = true, messagingSounds = true,
            socialSpy = false, staffChatVisible = true, loaded = false;

    private long credit, lastLoginTime;
    private Long firstLoginTime;

    private List<UUID> alts = new ArrayList<>();
    private List<UUID> ignores = new ArrayList<>();

    private List<Punishment> punishments = new ArrayList<>();

    private UUID lastRepliedPlayer;

    private boolean scoreboardMinimized;

    private Grant activeGrant;
    private List<Grant> grants = new ArrayList<>();

    public Profile(UUID uniqueUUID, String name) {
        this.uniqueUUID = uniqueUUID;
        this.name = name;

        this.lastRepliedPlayer = uniqueUUID;
        scoreboardMinimized = false;

        this.loadProfile(uniqueUUID);
    }

    public void setActiveGrant(Grant grant) {
        this.activeGrant = grant;
        Player player = toPlayer();
        if (player != null)
            player.setDisplayName(String.valueOf(grant.getRank().getPrefix()) + ((this.name == null) ? player.getName() : getName()));
    }

    public void activateNextGrant() {
        List<Grant> grants = new ArrayList<>(this.grants);
        grants.sort(Comparator.comparingInt(grant -> grant.getRank().getWeight()));
        for (Grant grant : grants) {
            if (!grant.isRemoved() && !grant.hasExpired())
                setActiveGrant(grant);
        }
    }

    public void checkGrants() {
        for (Grant grant : this.grants) {
            if (!grant.isRemoved() && grant.hasExpired()) {
                grant.setRemovedAt(System.currentTimeMillis());
                grant.setRemovedReason("Grant expired");
                grant.setRemoved(true);
                if (this.activeGrant != null && this.activeGrant.equals(grant))
                    this.activeGrant = null;
                Player player = toPlayer();
                if (player == null)
                    continue;
                Bukkit.getPluginManager().callEvent(new GrantExpireEvent(player, grant));
            }
        }

        if (this.activeGrant == null) {
            activateNextGrant();
            if (this.activeGrant != null)
                return;
            Grant grant = new Grant(UUID.randomUUID(), Rank.getDefaultRank(), null, System.currentTimeMillis(), "Default", 2147483647L);
            this.grants.add(grant);
            setActiveGrant(grant);
            Player player = toPlayer();
            if (player != null)
                Bukkit.getPluginManager().callEvent(new GrantAppliedEvent(player, grant));
        }
    }

    public Profile(Document document) {
        this.loadProfile(document);
    }

    private void loadProfile(UUID uniqueUUID) {
        Document documentUUID = RedrockMongoAPI.find("user", Filters.eq("uniqueUUID", uniqueUUID.toString()));
        if (documentUUID == null) {
            this.saveProfile();
            this.loaded = true;
            return;
        }

        loadProfile(documentUUID);
    }

    private void loadProfile(Document document) {
        this.uniqueUUID = UUID.fromString(document.getString("uniqueUUID"));
        this.name = document.getString("name");

        this.ipAddresses = new Gson().fromJson(document.getString("ipAddresses"), new TypeToken<List<String>>() {
        }.getType());
        this.currentAddress = document.getString("currentAddress");

        this.symbolType = SymbolType.getSymbolOrDefault(document.getString("symbolType"));

        this.credit = document.getLong("credit");
        this.staffMode = document.getBoolean("staffMode", false);
        this.staffChat = document.getBoolean("staffChat", false);
        this.vanishStatus = document.getBoolean("vanishStatus", false);
        this.frozenStatus = document.getBoolean("frozenStatus", false);

        this.firstLoginTime = document.getLong("firstLoginTime");
        this.lastLoginTime = document.getLong("lastLoginTime");

        this.messagesVisible = document.getBoolean("messagesVisible", true);
        this.messagingSounds = document.getBoolean("messagingSounds", true);

        this.socialSpy = document.getBoolean("socialSpy", false);

        this.staffChatVisible = document.getBoolean("staffChatVisible", true);

        this.alts = new Gson().fromJson(document.getString("alts"), new TypeToken<List<UUID>>() {
        }.getType());

        this.ignores = new Gson().fromJson(document.getString("ignores"), new TypeToken<List<UUID>>() {
        }.getType());

        JsonArray grantList = new JsonParser().parse(document.getString("grants")).getAsJsonArray();

        for (JsonElement grantData : grantList) {
            Grant grant = Grant.DESERIALIZER.deserialize(grantData.getAsJsonObject());

            if (grant != null) {
                this.grants.add(grant);
            }
        }

        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        for (JsonElement punishment : new JsonParser().parse(document.getString("punishments")).getAsJsonArray()) {
            this.punishments.add(punishmentManager.jsonPunishment(punishment.getAsJsonObject()));
        }

        this.lastRepliedPlayer = UUID.fromString(document.getString("lastRepliedPlayer"));

        this.scoreboardMinimized = document.getBoolean("scoreboardMinimized");

        activateNextGrant();
        checkGrants();
        this.loaded = true;
        }

    public void saveProfile() {
        Document document = new Document();
        document.put("uniqueUUID", this.uniqueUUID.toString());

        document.put("name", this.name);

        document.put("symbolType", this.symbolType.getDisplayName().toLowerCase(Locale.ENGLISH));
        document.put("credit", this.credit);
        document.put("staffMode", this.staffMode);
        document.put("staffChat", this.staffChat);
        document.put("vanishStatus", this.vanishStatus);
        document.put("frozenStatus", this.frozenStatus);

        document.put("firstLoginTime", this.firstLoginTime);
        document.put("lastLoginTime", this.lastLoginTime);

        document.put("currentAddress", this.currentAddress);
        document.put("ipAddresses", new Gson().toJson(this.ipAddresses, new TypeToken<List<String>>() {
        }.getType()));

        document.put("messagesVisible", this.messagesVisible);
        document.put("messagingSounds", this.messagingSounds);
        document.put("socialSpy", this.socialSpy);
        document.put("staffChatVisible", this.staffChatVisible);

        document.put("alts", new Gson().toJson(this.alts, new TypeToken<List<UUID>>() {
        }.getType()));

        document.put("ignores", new Gson().toJson(this.ignores, new TypeToken<List<UUID>>() {
        }.getType()));


        JsonArray grants = new JsonArray();
        for (Grant grant : this.grants)
            grants.add((JsonElement)Grant.SERIALIZER.serialize(grant));

        document.put("grants", grants.toString());

        JsonArray jsonPunishments = new JsonArray();
        for (Punishment punishment : this.punishments) {
            jsonPunishments.add(punishment.getJsonObject());
        }

        document.put("punishments", jsonPunishments.toString());

        document.put("lastRepliedPlayer", this.lastRepliedPlayer.toString());

        document.put("scoreboardMinimized", this.scoreboardMinimized);

        RedrockMongoAPI.replaceOne("user", Filters.eq("uniqueUUID", this.uniqueUUID.toString()), document, true);
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(this.uniqueUUID);
    }

    public void setupPermissionsAttachment(Player player) {
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null)
                continue;
            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
        }

        PermissionAttachment attachment = player.addAttachment(RedrockPlugin.getInstance());
        for (String perm : this.activeGrant.getRank().getAllPermissions())
            attachment.setPermission(perm, true);
        player.recalculatePermissions();
    }

    public boolean hasIPAddress(String ipAddress) {
        return this.ipAddresses.contains(ipAddress);
    }

    public void addIPAddress(String ipAddress) {
        this.ipAddresses.add(ipAddress);
    }

    public Rank getActiveRank() {
        return this.activeGrant.getRank();
    }

    public Grant getActiveGrant() {
        return this.activeGrant;
    }

    public List<Grant> getGrants() {
        return this.grants;
    }

}
