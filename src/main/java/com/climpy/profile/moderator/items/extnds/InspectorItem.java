package com.climpy.profile.moderator.items.extnds;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.moderator.items.StaffModeItem;
import com.climpy.profile.moderator.menu.InspectorMenu;
import com.climpy.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InspectorItem extends StaffModeItem {

    public InspectorItem() {
        super(ItemUtils.createItem(Material.BOOK, "&6İncele", 1), 1);
    }

    @Override
    public void onCustomInteractEvent(Event event) {
        if (event instanceof PlayerInteractEntityEvent) {
            PlayerInteractEntityEvent playerInteractEntityEvent = (PlayerInteractEntityEvent) event;
            Player player = playerInteractEntityEvent.getPlayer();
            Player targetPlayer = (Player) playerInteractEntityEvent.getRightClicked();
            ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
            Profile profile = profileManager.getProfile(player.getUniqueId());
            Profile targetProfile = profileManager.getProfile(targetPlayer.getUniqueId());
            if (profile == null) {
                return;
            }

            InspectorMenu.getInventory(targetProfile).open(targetPlayer);
        }
    }

}
