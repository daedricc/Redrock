package com.climpy.profile.moderator.items;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface StaffModeItemProvider {

    void applyToPlayer(Player player);

    void removeToPlayer(Player player);

    void onCustomInteractEvent(Event event);

    void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event);

    void onPlayerInteractEvent(PlayerInteractEvent event);

    boolean hasEquals(ItemStack itemStack);
}
