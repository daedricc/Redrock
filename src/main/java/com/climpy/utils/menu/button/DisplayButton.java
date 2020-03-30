package com.climpy.utils.menu.button;

import com.climpy.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DisplayButton extends Button {
    private ItemStack itemStack;

    private boolean cancel;

    public ItemStack getButtonItem(Player player) {
        if (this.itemStack == null)
            return new ItemStack(Material.AIR);
        return this.itemStack;
    }

    public boolean shouldCancel(Player player, ClickType clickType) {
        return this.cancel;
    }

    public DisplayButton(ItemStack itemStack, boolean cancel) {
        this.itemStack = itemStack;
        this.cancel = cancel;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public boolean isCancel() {
        return this.cancel;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
