package com.climpy.profile.moderator.listener;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.moderator.StaffManager;
import com.climpy.profile.moderator.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StaffListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        StaffManager staffManager = RedrockPlugin.getInstance().getStaffManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = event.getPlayer();

        Profile profile = profileManager.getProfile(player.getUniqueId());
        if (profile.isFrozenStatus()) {
            if (profile.isStaffMode() || profile.isStaffChat() || profile.isVanishStatus()) {
                profile.setStaffChat(false);
                profile.setVanishStatus(false);
                profile.setStaffMode(false);
                profile.saveProfile();

                staffManager.setStaffMode(profile.getUniqueUUID(), false);
                vanishManager.setVanishStatus(profile.getUniqueUUID(), false);
            }

        } else if (profile.toPlayer().hasPermission("redrock.mod") && (profile.isVanishStatus() || profile.isStaffMode())) {
            Bukkit.getScheduler().runTaskLater(RedrockPlugin.getInstance(), ()-> {
                if (!staffManager.getStaffs().contains(profile.getUniqueUUID())) {
                    staffManager.setStaffMode(profile.getUniqueUUID(), true);
                }
            }, 5L);
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        StaffManager staffManager = RedrockPlugin.getInstance().getStaffManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = event.getPlayer();

        Profile profile = profileManager.getProfile(player.getUniqueId());
        if (staffManager.getStaffs().contains(profile.getUniqueUUID())) {
            staffManager.setStaffMode(profile.getUniqueUUID(), false);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        StaffManager staffManager = RedrockPlugin.getInstance().getStaffManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = (Player) event.getEntity();

        Profile profile = profileManager.getProfile(player.getUniqueId());
        if (staffManager.getStaffs().contains(profile.getUniqueUUID())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        StaffManager staffManager = RedrockPlugin.getInstance().getStaffManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = (Player) event.getWhoClicked();

        Profile profile = profileManager.getProfile(player.getUniqueId());
        if (staffManager.getStaffs().contains(profile.getUniqueUUID())) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null) return;

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null || itemMeta.getDisplayName() == null) {
                return;
            }

            if (staffManager.hasStaffModeItem(itemStack)) {
                event.setCancelled(true);
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        StaffManager staffManager = RedrockPlugin.getInstance().getStaffManager();
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Player player = event.getPlayer();

        Profile profile = profileManager.getProfile(player.getUniqueId());
        if (staffManager.getStaffs().contains(profile.getUniqueUUID())) {
            ItemStack itemStack = event.getItemDrop().getItemStack();
            if (itemStack == null) return;

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null || itemMeta.getDisplayName() == null) {
                return;
            }

            if (staffManager.hasStaffModeItem(itemStack)) {
                event.setCancelled(true);
            }
        }
    }
}
