package com.climpy.deathmessage.tracker;

import com.climpy.deathmessage.damage.Damage;
import com.climpy.deathmessage.damage.PlayerDamage;
import com.climpy.deathmessage.event.CustomPlayerDamageEvent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public final class PvPTracker implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (!(event.getCause() instanceof EntityDamageByEntityEvent)) {
            return;
        }
        final EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event.getCause();
        final Entity damager = damageByEntityEvent.getDamager();
        if (damager instanceof Player) {
            final Player damaged = event.getPlayer();
            event.setTrackerDamage(new PvPDamage(damaged, event.getDamage(), (Player) damager));
        }
    }

    public static class PvPDamage extends PlayerDamage {
        private final String itemString;

        public PvPDamage(final Player damaged, final double damage, final Player damager) {
            super(damaged, damage, damager);
            final ItemStack hand = damager.getItemInHand();
            if (hand.getType() == Material.AIR) {
                this.itemString = "their fists";
            } else if (hand.getItemMeta().hasDisplayName()) {
                this.itemString = ChatColor.stripColor(hand.getItemMeta().getDisplayName());
            } else {
                this.itemString = WordUtils.capitalizeFully(hand.getType().name().replace('_', ' '));
            }
        }

        @Override
        public String getDeathMessage(final Player getFor) {
            return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + ", " + Damage.wrapName(this.getDamager(), getFor) + " tarafından öldürüldü.";
        }
    }
}
