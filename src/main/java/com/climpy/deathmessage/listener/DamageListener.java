package com.climpy.deathmessage.listener;

import com.climpy.RedrockPlugin;
import com.climpy.deathmessage.RedrockDeathMessageHandler;
import com.climpy.deathmessage.damage.UnknownDamage;
import com.climpy.deathmessage.event.CustomPlayerDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public final class DamageListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final CustomPlayerDamageEvent customEvent = new CustomPlayerDamageEvent(event);
            customEvent.setTrackerDamage(new UnknownDamage(player, customEvent.getDamage()));
            RedrockPlugin.getInstance().getServer().getPluginManager().callEvent((Event) customEvent);
            RedrockDeathMessageHandler.addDamage(player, customEvent.getTrackerDamage());
        }
    }
}
