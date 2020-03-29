package com.climpy.profile.moderator.items.extnds;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.moderator.VanishManager;
import com.climpy.profile.moderator.items.StaffModeItem;
import com.climpy.utils.C;
import com.climpy.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
public class VanishChangeItem extends StaffModeItem {
    public VanishChangeItem() {
        super(null, 7);
        this.changeVanishItem(true);
    }

    @Override
    public void onCustomInteractEvent(Event event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
            Player player = playerInteractEvent.getPlayer();
            VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
            ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
            Profile profile = profileManager.getProfile(player.getUniqueId());
            if(profile == null) {
                return;
            }

            boolean newVanishStatus = !profile.isVanishStatus();
            profile.setVanishStatus(newVanishStatus);
            profile.saveProfile();

            this.changeVanishItem(newVanishStatus).applyToPlayer(player);
            vanishManager.setVanishStatus(profile.getUniqueUUID(), newVanishStatus);
        }
    }

    public VanishChangeItem changeVanishItem(boolean newVanishStatus) {
        this.itemStack = ItemUtils.createItem(Material.INK_SACK, C.AQUA + (newVanishStatus ? "Görünür Ol" : "Görünmez Ol"), (newVanishStatus ? (short) 8 : (short) 10), Enchantment.DURABILITY);
        return this;
    }
}