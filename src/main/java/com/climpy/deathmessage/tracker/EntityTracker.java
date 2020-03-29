package com.climpy.deathmessage.tracker;

import com.climpy.deathmessage.damage.Damage;
import com.climpy.deathmessage.damage.MobDamage;
import com.climpy.deathmessage.event.CustomPlayerDamageEvent;
import com.climpy.utils.EntityUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class EntityTracker implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (!(event.getCause() instanceof EntityDamageByEntityEvent)) {
            return;
        }
        final EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event.getCause();
        final Entity damager = damageByEntityEvent.getDamager();
        if (damager instanceof LivingEntity && !(damager instanceof Player)) {
            event.setTrackerDamage(new EntityDamage(event.getPlayer(), event.getDamage(), damager));
        }
    }

    public static class EntityDamage extends MobDamage {
        public EntityDamage(final Player damaged, final double damage, final Entity entity) {
            super(damaged, damage, entity.getType());
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + ", " + ChatColor.RED + EntityUtils.getName(this.getMobType()) + ChatColor.YELLOW + " tarafından öldürüldü.";
        }
    }
}
