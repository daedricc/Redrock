package com.climpy.profile.moderator.items.extnds;

import com.climpy.profile.moderator.items.StaffModeItem;
import com.climpy.profile.moderator.menu.OnlineStaffMenu;
import com.climpy.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnlineStaffMenuItem extends StaffModeItem {

    public OnlineStaffMenuItem() {
        super(ItemUtils.createItem(Material.SKULL_ITEM, "&6Çevrimiçi Yetkililer", 1), 4);
    }

    @Override
    public void onCustomInteractEvent(Event event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
            Player player = playerInteractEvent.getPlayer();
            OnlineStaffMenu.getInventory().open(player);
        }
    }
}