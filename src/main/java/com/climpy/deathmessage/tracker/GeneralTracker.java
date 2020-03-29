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

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class GeneralTracker implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        String message = null;
        switch (event.getCause().getCause()) {
            case SUFFOCATION: {
                message = "blok içinde sıkışarak öldü";
                break;
            }
            case DROWNING: {
                message = "boğularak öldü";
                break;
            }
            case STARVATION: {
                message = "açlıktan öldü";
                break;
            }
            case LIGHTNING: {
                message = "yıldırım çarparak öldü";
                break;
            }
            case POISON: {
                message = "zehirlendi";
                break;
            }
            case WITHER: {
                message = "wither tarafından çarpıldı";
                break;
            }
            case CONTACT: {
                message = "delindi";
                break;
            }
            case ENTITY_EXPLOSION:
            case BLOCK_EXPLOSION: {
                message = "patlatılarak öldü";
                break;
            }
            default: {
                return;
            }
        }
        final List<Damage> record = RedrockDeathMessageHandler.getDamage(event.getPlayer());
        Damage knocker = null;
        long knockerTime = 0L;
        for (final Damage damage : record) {
            if (!(damage instanceof GeneralDamage)) {
                if (damage instanceof GeneralDamageByPlayer) {
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
            event.setTrackerDamage(new GeneralDamageByPlayer(event.getPlayer(), event.getDamage(), ((PlayerDamage) knocker).getDamager(), message));
        } else {
            event.setTrackerDamage(new GeneralDamage(event.getPlayer(), event.getDamage(), message));
        }
    }

    public static class GeneralDamage extends Damage {
        private final String message;

        public GeneralDamage(final Player damaged, final double damage, final String message) {
            super(damaged, damage);
            this.message = message;
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + " " + ChatColor.YELLOW + this.message + ".";
        }
    }

    public static class GeneralDamageByPlayer extends PlayerDamage {
        private final String message;

        public GeneralDamageByPlayer(final Player damaged, final double damage, final Player damager, final String message) {
            super(damaged, damage, damager);
            this.message = message;
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + " " + ChatColor.YELLOW + this.message + ", " + Damage.wrapName(this.getDamager(), getFor) + ChatColor.YELLOW + " ile savaşırken ";
        }
    }
}
