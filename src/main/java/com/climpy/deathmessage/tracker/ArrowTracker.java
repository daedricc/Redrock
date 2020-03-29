package com.climpy.deathmessage.tracker;

import com.climpy.RedrockPlugin;
import com.climpy.deathmessage.damage.Damage;
import com.climpy.deathmessage.damage.MobDamage;
import com.climpy.deathmessage.damage.PlayerDamage;
import com.climpy.deathmessage.event.CustomPlayerDamageEvent;
import com.climpy.utils.EntityUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public final class ArrowTracker implements Listener {
    @EventHandler
    public void onEntityShootBow(final EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            event.getProjectile().setMetadata("ShotFromDistance", (MetadataValue) new FixedMetadataValue((Plugin) RedrockPlugin.getInstance(), (Object) event.getProjectile().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (!(event.getCause() instanceof EntityDamageByEntityEvent)) {
            return;
        }
        final EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event.getCause();
        if (!(damageByEntityEvent.getDamager() instanceof Arrow)) {
            return;
        }
        final Arrow arrow = (Arrow) damageByEntityEvent.getDamager();
        if (arrow.getShooter() instanceof Player) {
            final Player shooter = (Player) arrow.getShooter();
            for (final MetadataValue value : arrow.getMetadata("ShotFromDistance")) {
                final Location shotFrom = (Location) value.value();
                final double distance = shotFrom.distance(event.getPlayer().getLocation());
                event.setTrackerDamage(new ArrowDamageByPlayer(event.getPlayer(), event.getDamage(), shooter, distance));
            }
        } else if (arrow.getShooter() != null) {
            if (arrow.getShooter() instanceof Entity) {
                event.setTrackerDamage(new ArrowDamageByMob(event.getPlayer(), event.getDamage(), (Entity) arrow.getShooter()));
            }
        } else {
            event.setTrackerDamage(new ArrowDamage(event.getPlayer(), event.getDamage()));
        }
    }

    public static class ArrowDamage extends Damage {
        public ArrowDamage(final Player damaged, final double damage) {
            super(damaged, damage);
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + " vuruldu.";
        }
    }

    public static class ArrowDamageByPlayer extends PlayerDamage {
        private final double distance;

        public ArrowDamageByPlayer(final Player damaged, final double damage, final Player damager, final double distance) {
            super(damaged, damage, damager);
            this.distance = distance;
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + ", " + Damage.wrapName(this.getDamager(), getFor) + ChatColor.YELLOW + " tarafından vuruldu. (" + ChatColor.BLUE + (int) this.distance + " blok" + ChatColor.YELLOW + ")";
        }
    }

    public static class ArrowDamageByMob extends MobDamage {
        public ArrowDamageByMob(final Player damaged, final double damage, final Entity damager) {
            super(damaged, damage, damager.getType());
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + ", " + ChatColor.RED + EntityUtils.getName(this.getMobType()) + ChatColor.YELLOW + " tarafından vuruldu.";
        }
    }
}
