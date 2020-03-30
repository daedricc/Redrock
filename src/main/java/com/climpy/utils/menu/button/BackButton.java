package com.climpy.utils.menu.button;

import com.climpy.utils.C;
import com.climpy.utils.ItemMaker;
import com.climpy.utils.menu.Button;
import com.climpy.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button {
    private Menu back;

    public ItemStack getButtonItem(Player player) {
        return (new ItemMaker(Material.REDSTONE)).setTitle(String.valueOf(C.RED) + C.BOLD + "Geri").
                setLore(new String[] { String.valueOf(C.RED) + "Önceki menüye dönmek için ", String.valueOf(C.RED) + " tıklayabilirsin." }).build();
    }

    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }

    public BackButton(Menu back) {
        this.back = back;
    }
}

