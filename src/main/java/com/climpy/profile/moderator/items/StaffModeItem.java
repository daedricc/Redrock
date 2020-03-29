package com.climpy.profile.moderator.items;

import com.climpy.RedrockPlugin;

import lombok.Data;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter @Data
public abstract class StaffModeItem implements StaffModeItemProvider, Listener {
    protected ItemStack itemStack;
    private int slot;

    public StaffModeItem(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;

        Bukkit.getPluginManager().registerEvents(this, RedrockPlugin.getInstance());
    }

    @Override
    public void applyToPlayer(Player player) {
        player.getInventory().setItem(this.slot, this.itemStack);
    }

    @Override
    public void removeToPlayer(Player player) {
        player.getInventory().remove(this.slot);
    }

    @Override
    public abstract void onCustomInteractEvent(Event event);

    @Override
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();

        if (entity instanceof Player) {
            ItemStack itemStack = player.getItemInHand();
            if (itemStack == null) return;

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null || itemMeta.getDisplayName() == null) {
                return;
            }

            if (this.hasEquals(itemStack)) {
                event.setCancelled(true);

                if (player.hasMetadata("staffmode")) {
                    this.onCustomInteractEvent(event);
                } else {
                    player.sendMessage(ChatColor.RED + "Bu eşyayı kullanmak için moderatör modunuzun açık olması gereklidir.");
                    player.getInventory().remove(itemStack);
                }
            }
        }

    }

    @Override
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || itemMeta.getDisplayName() == null) {
            return;
        }

        if (event.hasItem() && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (this.hasEquals(itemStack)) {
                event.setCancelled(true);

                if (player.hasMetadata("staffmode")) {
                    this.onCustomInteractEvent(event);
                } else {
                    player.sendMessage(ChatColor.RED + "Bu eşyayı kullanmak için moderatör modunuzun açık olması gereklidir.");
                    player.getInventory().remove(itemStack);
                }
            }
        }

    }

    @Override
    public boolean hasEquals(ItemStack itemStack) {
        return this.equals(itemStack);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ItemStack) {
            ItemStack targetItem = (ItemStack) object;

            ItemMeta targetItemMeta = targetItem.getItemMeta();
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            return this.itemStack.getType() == targetItem.getType() &&
                    itemMeta.getDisplayName().equals(targetItemMeta.getDisplayName()) &&
                    this.itemStack.getDurability() == targetItem.getDurability();
        }

        return false;
    }

}
