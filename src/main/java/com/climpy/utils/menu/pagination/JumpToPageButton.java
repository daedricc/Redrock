package com.climpy.utils.menu.pagination;

import com.climpy.utils.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class JumpToPageButton extends Button {
    private int page;

    private PaginatedMenu menu;

    private boolean current;

    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Sayfa " + this.page);
        if (this.current)
            itemMeta.setLore(Arrays.asList(new String[]{"", ChatColor.GREEN + "Mevcut Sayfa"}));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void clicked(Player player, ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        Button.playNeutral(player);
    }

    public JumpToPageButton(int page, PaginatedMenu menu, boolean current) {
        this.page = page;
        this.menu = menu;
        this.current = current;
    }
}
