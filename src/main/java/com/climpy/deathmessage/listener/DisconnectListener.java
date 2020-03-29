package com.climpy.deathmessage.listener;

import com.climpy.deathmessage.RedrockDeathMessageHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class DisconnectListener implements Listener {
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        RedrockDeathMessageHandler.clearDamage(event.getPlayer());
    }
}
