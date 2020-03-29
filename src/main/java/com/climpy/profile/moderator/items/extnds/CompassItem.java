package com.climpy.profile.moderator.items.extnds;

import com.climpy.profile.moderator.items.StaffModeItem;
import com.climpy.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;

public class CompassItem extends StaffModeItem {

    public CompassItem() {
        super(ItemUtils.createItem(Material.COMPASS, "&6Işınlanma Pusulası", 1), 0);
    }

    @Override
    public void onCustomInteractEvent(Event event) {
        // nothing beacuse nothing sq
    }
}
