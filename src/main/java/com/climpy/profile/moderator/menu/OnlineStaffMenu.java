package com.climpy.profile.moderator.menu;

import com.climpy.RedrockPlugin;
import com.climpy.inv.ClickableItem;
import com.climpy.inv.SmartInventory;
import com.climpy.inv.content.InventoryContents;
import com.climpy.inv.content.InventoryProvider;
import com.climpy.inv.content.SlotPos;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.redis.RedisPlayerAction;
import com.climpy.profile.redis.RedisPlayerCache;
import com.climpy.profile.redis.RedisPlayerData;
import com.climpy.punishments.history.menu.HistoryCategoryMenu;
import com.climpy.redis.RedisManager;
import com.climpy.server.ServerData;
import com.climpy.utils.ItemUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineStaffMenu implements InventoryProvider {

    public static SmartInventory getInventory() {
        return SmartInventory.builder()
                .provider(new OnlineStaffMenu())
                .size(1, 9)
                .title("&8Çevrimiçi Yetkililer")
                .build();
    }


    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        RedisPlayerCache redisPlayerCache = RedrockPlugin.getInstance().getRedisPlayerCache();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();

        List<Profile> profiles = profileManager.getDatabaseProfiles().stream().filter(profile -> {
            return profile.getRankType().isAboveOrEqual(RankType.MOD);
        }).collect(Collectors.toList());

        List<Profile> targetProfiles = profiles.stream().filter(profile -> {
            RedisPlayerData redisPlayerData = redisPlayerCache.getRedisPlayerData(profile.getUniqueUUID());
            return redisPlayerData != null && redisPlayerData.getRedisPlayerAction() == RedisPlayerAction.JOINING_SERVER;
        }).collect(Collectors.toList());

        RedisPlayerData redisPlayerData = redisPlayerCache.getRedisPlayerData(player.getUniqueId());
        for (int i = 0; i < targetProfiles.size(); i++) {
            Profile targetProfile = targetProfiles.get(i);

            RedisPlayerData targetPlayerRedisData = redisPlayerCache.getRedisPlayerData(targetProfile.getUniqueUUID());
            contents.set(SlotPos.of(0, i), ClickableItem.of(ItemUtils.createSkullItem(targetProfile.getRankType().getColor() + targetProfile.getName(), (short) 3, targetProfile.getName(), Arrays.asList(
                    "§7Sunucu: §f" + targetPlayerRedisData.getLastSeenServer(),
                    "§7Staff Modu: " + (targetProfile.isStaffMode() ? "§aAçık" : "§cKapalı"),
                    "§7Görünmezlik: " + (targetProfile.isVanishStatus() ? "§aAçık" : "§cKapalı"),
                    "",
                    (targetProfile.getUniqueUUID().equals(player.getUniqueId()) ? "§cBu hesap size ait." : "§eYanına gitmek için tıkla!")
            )), e -> {
                if (!targetProfile.getUniqueUUID().equals(player.getUniqueId())) {
                    if(targetPlayerRedisData.getLastSeenServer().equals(redisPlayerData.getLastSeenServer())) {
                        player.teleport(targetProfile.toPlayer());
                    } else {
                        ServerData serverData = redisManager.getServerByName(targetPlayerRedisData.getLastSeenServer());
                        redisManager.sendRedisServer(player.getUniqueId(), serverData);
                    }
                }
            }));

        }
    }
}
