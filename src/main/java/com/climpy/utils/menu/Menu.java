package com.climpy.utils.menu;

import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Menu {
    public static Map<String, Menu> currentlyOpenedMenus = new HashMap<>();

    private Map<Integer, Button> buttons = new HashMap<>();

    private boolean autoUpdate = false;

    private boolean updateAfterClick = true;

    private boolean closedByMenu = false;

    private boolean placeholder = false;

    private Button placeholderButton = Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, new String[] { " " });

    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getButtonItem(player);
        if (item.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName())
                meta.setDisplayName(String.valueOf(meta.getDisplayName()) + "§b§c§d§e");
                        item.setItemMeta(meta);
        }
        return item;
    }

    public void openMenu(Player player) {
        this.buttons = getButtons(player);
        Menu previousMenu = currentlyOpenedMenus.get(player.getName());
        Inventory inventory = null;
        int size = (getSize() == -1) ? size(this.buttons) : getSize();
        boolean update = false;
        String title = C.translate(getTitle(player));
        if (title.length() > 32)
            title = title.substring(0, 32);
        if (player.getOpenInventory() != null)
            if (previousMenu == null) {
                player.closeInventory();
            } else {
                int previousSize = player.getOpenInventory().getTopInventory().getSize();
                if (previousSize == size && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
                    inventory = player.getOpenInventory().getTopInventory();
                    update = true;
                } else {
                    previousMenu.setClosedByMenu(true);
                    player.closeInventory();
                }
            }
        if (inventory == null)
            inventory = Bukkit.createInventory((InventoryHolder)player, size, title);
        inventory.setContents(new ItemStack[inventory.getSize()]);
        currentlyOpenedMenus.put(player.getName(), this);
        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet())
            inventory.setItem(((Integer)buttonEntry.getKey()).intValue(), createItemStack(player, buttonEntry.getValue()));
        if (isPlaceholder())
            for (int index = 0; index < size; index++) {
                if (this.buttons.get(Integer.valueOf(index)) == null) {
                    this.buttons.put(Integer.valueOf(index), this.placeholderButton);
                    inventory.setItem(index, this.placeholderButton.getButtonItem(player));
                }
            }
        if (update) {
            player.updateInventory();
        } else {
            player.openInventory(inventory);
        }
        onOpen(player);
        setClosedByMenu(false);
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;
        for (Iterator<Integer> iterator = buttons.keySet().iterator(); iterator.hasNext(); ) {
            int buttonValue = ((Integer)iterator.next()).intValue();
            if (buttonValue > highest)
                highest = buttonValue;
        }
        return (int)(Math.ceil((highest + 1) / 9.0D) * 9.0D);
    }

    public int getSize() {
        return -1;
    }

    public int getSlot(int x, int y) {
        return 9 * y + x;
    }

    public abstract String getTitle(Player paramPlayer);

    public abstract Map<Integer, Button> getButtons(Player paramPlayer);

    public void onOpen(Player player) {}

    public void onClose(Player player) {}

    public Map<Integer, Button> getButtons() {
        return this.buttons;
    }

    public boolean isAutoUpdate() {
        return this.autoUpdate;
    }

    public boolean isUpdateAfterClick() {
        return this.updateAfterClick;
    }

    public boolean isClosedByMenu() {
        return this.closedByMenu;
    }

    public boolean isPlaceholder() {
        return this.placeholder;
    }

    public Button getPlaceholderButton() {
        return this.placeholderButton;
    }

    public void setButtons(Map<Integer, Button> buttons) {
        this.buttons = buttons;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public void setUpdateAfterClick(boolean updateAfterClick) {
        this.updateAfterClick = updateAfterClick;
    }

    public void setClosedByMenu(boolean closedByMenu) {
        this.closedByMenu = closedByMenu;
    }

    public void setPlaceholder(boolean placeholder) {
        this.placeholder = placeholder;
    }

    public void setPlaceholderButton(Button placeholderButton) {
        this.placeholderButton = placeholderButton;
    }
}
