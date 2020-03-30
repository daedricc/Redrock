package com.climpy.utils.menu.button;

import com.climpy.utils.menu.Button;
import com.climpy.utils.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class JumpToMenuButton extends Button {
    private Menu menu;

    private ItemStack itemStack;

    public JumpToMenuButton(Menu menu, ItemStack itemStack) {
        this.menu = menu;
        this.itemStack = itemStack;
    }

    public ItemStack getButtonItem(Player player) {
        return this.itemStack;
    }

    public void clicked(Player player, ClickType clickType) {
        this.menu.openMenu(player);
    }
}
