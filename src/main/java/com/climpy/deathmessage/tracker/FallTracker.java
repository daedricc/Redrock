package com.climpy.deathmessage.tracker;

import com.climpy.deathmessage.RedrockDeathMessageHandler;
import com.climpy.deathmessage.damage.Damage;
import com.climpy.deathmessage.damage.PlayerDamage;
import com.climpy.deathmessage.event.CustomPlayerDamageEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class FallTracker implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause().getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        final List<Damage> record = RedrockDeathMessageHandler.getDamage(event.getPlayer());
        Damage knocker = null;
        long knockerTime = 0L;
        for (final Damage damage : record) {
            if (!(damage instanceof FallDamage)) {
                if (damage instanceof FallDamageByPlayer) {
                    continue;
                }
                if (!(damage instanceof PlayerDamage) || (knocker != null && damage.getTime() <= knockerTime)) {
                    continue;
                }
                knocker = damage;
                knockerTime = damage.getTime();
            }
        }
        if (knocker != null && knockerTime + TimeUnit.MINUTES.toMillis(1L) > System.currentTimeMillis()) {
            event.setTrackerDamage(new FallDamageByPlayer(event.getPlayer(), event.getDamage(), ((PlayerDamage) knocker).getDamager()));
        } else {
            event.setTrackerDamage(new FallDamage(event.getPlayer(), event.getDamage()));
        }
    }

    public static class FallDamage extends Damage {
        public FallDamage(final Player damaged, final double damage) {
            super(damaged, damage);
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + " yere çok sert çarptı.";
        }
    }

    public static class FallDamageByPlayer extends PlayerDamage {
        public FallDamageByPlayer(final Player damaged, final double damage, final Player damager) {
            super(damaged, damage, damager);
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + ", " + Damage.wrapName(this.getDamager(), getFor) + ChatColor.YELLOW + " sayesinde yere yapıştı.";
        }
    }
}
