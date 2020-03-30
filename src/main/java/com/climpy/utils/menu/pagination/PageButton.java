package com.climpy.utils.menu.pagination;

import com.climpy.utils.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PageButton extends Button {
    private int mod;

    private PaginatedMenu menu;

    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (hasNext(player)) {
            itemMeta.setDisplayName((this.mod > 0) ? (ChatColor.GREEN + "İleriki Sayfa") : (ChatColor.RED + "Önceki Sayfa"));
        } else {
            itemMeta.setDisplayName(ChatColor.RED + ((this.mod > 0) ? "Son Sayfa" : "İlk Sayfa"));
        }
        itemMeta.setLore(Arrays.asList(new String[] { ChatColor.GRAY + "Buraya basarak ", ChatColor.GRAY + "bir sayfaya atlayabilirsiniz." }));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            (new ViewAllPagesMenu(this.menu)).openMenu(player);
            Button.playNeutral(player);
        } else if (hasNext(player)) {
            this.menu.modPage(player, this.mod);
            Button.playNeutral(player);
        } else {
            Button.playFail(player);
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return (pg > 0 && this.menu.getPages(player) >= pg);
    }

    public PageButton(int mod, PaginatedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }
}
