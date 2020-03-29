package com.climpy.profile.moderator.listener;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.moderator.StaffManager;
import com.climpy.profile.moderator.VanishManager;
import com.climpy.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class VanishListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = event.getPlayer();

        for (UUID uniqueUUID : vanishManager.getVanished()) {
            Profile targetUser = profileManager.getProfile(uniqueUUID);
            if (targetUser == null || !targetUser.isVanishStatus()) {
                continue;
            }

            Player targetPlayer = targetUser.toPlayer();
            Bukkit.getScheduler().runTask(RedrockPlugin.getInstance(), () -> {
                if (player.canSee(targetPlayer)) {
                    player.hidePlayer(targetPlayer);
                }
            });
        }
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event) {
        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = event.getPlayer();
        Profile profile = profileManager.getProfile(player.getUniqueId());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (player.canSee(onlinePlayer)) {
                continue;
            }

            player.showPlayer(onlinePlayer);
        }

        if (vanishManager.getVanished().contains(profile.getUniqueUUID())) {
            vanishManager.setVanishStatus(profile.getUniqueUUID(), false);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        StaffManager staffManager = RedrockPlugin.getInstance().getStaffManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = event.getPlayer();
        Profile profile = profileManager.getProfile(player.getUniqueId());

        if (staffManager.getStaffs().contains(profile.getUniqueUUID())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = event.getPlayer();
        Profile profile = profileManager.getProfile(player.getUniqueId());

        if (vanishManager.getVanished().contains(profile.getUniqueUUID())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = (Player) event.getDamager();
        Profile profile = profileManager.getProfile(player.getUniqueId());

        if (vanishManager.getVanished().contains(profile.getUniqueUUID())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageEntity(EntityDamageEvent event) {
        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = (Player) event.getEntity();
        Profile profile = profileManager.getProfile(player.getUniqueId());

        if (vanishManager.getVanished().contains(profile.getUniqueUUID())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageEntity(EntityTargetLivingEntityEvent event) {
        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = (Player) event.getEntity();
        Profile profile = profileManager.getProfile(player.getUniqueId());

        if (vanishManager.getVanished().contains(profile.getUniqueUUID())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = event.getPlayer();
        Profile profile = profileManager.getProfile(player.getUniqueId());

        if (vanishManager.getVanished().contains(profile.getUniqueUUID())) {
            event.setCancelled(true);
        }
    }


}
