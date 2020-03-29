package com.climpy.profile.moderator.items.extnds;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.moderator.items.StaffModeItem;
import com.climpy.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerFrozenItem extends StaffModeItem {

    public PlayerFrozenItem() {
        super(ItemUtils.createItem(Material.CARPET, "&6Oyuncuyu Dondur", 1), 2);
    }

    @Override
    public void onCustomInteractEvent(Event event) {
        if (event instanceof PlayerInteractEntityEvent) {
            PlayerInteractEntityEvent playerInteractEntityEvent = (PlayerInteractEntityEvent) event;
            Player targetPlayer = (Player) playerInteractEntityEvent.getRightClicked();
            Player player = playerInteractEntityEvent.getPlayer();

            ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
            Profile profile = profileManager.getProfile(player.getUniqueId());

            Profile targetProfile = profileManager.getProfile(targetPlayer.getUniqueId());
            if (profile == null || targetProfile == null) {
                return;
            }

           /* FrozenManager frozenManager = ModSuitePlugin.getInstance().getFrozenManager();
            frozenManager.setFrozenPlayer(player, targetProfile.getUniqueUUID()); */
        }
    }
}
