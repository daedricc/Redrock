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

public final class VoidTracker implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause().getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }
        final List<Damage> record = RedrockDeathMessageHandler.getDamage(event.getPlayer());
        Damage knocker = null;
        long knockerTime = 0L;
        for (final Damage damage : record) {
            if (!(damage instanceof VoidDamage)) {
                if (damage instanceof VoidDamageByPlayer) {
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
            event.setTrackerDamage(new VoidDamageByPlayer(event.getPlayer(), event.getDamage(), ((PlayerDamage) knocker).getDamager()));
        } else {
            event.setTrackerDamage(new VoidDamage(event.getPlayer(), event.getDamage()));
        }
    }

    public static class VoidDamage extends Damage {
        public VoidDamage(final Player damaged, final double damage) {
            super(damaged, damage);
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + " boşluğa düştü.";
        }
    }

    public static class VoidDamageByPlayer extends PlayerDamage {
        public VoidDamageByPlayer(final Player damaged, final double damage, final Player damager) {
            super(damaged, damage, damager);
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + ", " + Damage.wrapName(this.getDamager(), getFor) + ChatColor.YELLOW + " sayesinde boşluğa düştü.";
        }
    }
}
