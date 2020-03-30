package com.climpy.utils.menu.button;

import com.climpy.utils.menu.Button;
import com.climpy.utils.menu.Menu;
import com.climpy.utils.menu.callback.TypeCallback;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfirmationButton extends Button {
    private boolean confirm;

    private TypeCallback<Boolean> callback;

    private boolean closeAfterResponse;

    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.WOOL, 1, (short)(this.confirm ? 5 : 14));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.confirm ? (ChatColor.GREEN + "Confirm") : (ChatColor.RED + "Cancel"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void clicked(Player player, ClickType clickType) {
        if (this.confirm) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20.0F, 0.1F);
        } else {
            player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20.0F, 0.1F);
        }
        if (this.closeAfterResponse) {
            Menu menu = (Menu)Menu.currentlyOpenedMenus.get(player.getName());
            if (menu != null)
                menu.setClosedByMenu(true);
            player.closeInventory();
        }
        this.callback.callback(Boolean.valueOf(this.confirm));
    }

    public ConfirmationButton(boolean confirm, TypeCallback<Boolean> callback, boolean closeAfterResponse) {
        this.confirm = confirm;
        this.callback = callback;
        this.closeAfterResponse = closeAfterResponse;
    }
}
