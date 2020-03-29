//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.climpy.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemUtils {
    public ItemUtils() {
    }

    public static ItemStack createSkullItem(String itemName, String customSkullURL) {
        return createSkullItem(itemName, customSkullURL, (List) null);
    }

    public static ItemStack createSkullItem(String itemName, String customSkullURL, List<String> lore) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (customSkullURL.isEmpty()) {
            return itemStack;
        } else {
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
            Field profileField = null;

            try {
                profileField = itemMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
            } catch (IllegalArgumentException | NoSuchFieldException var7) {
                var7.printStackTrace();
            }

            itemMeta.setDisplayName(C.translate(itemName));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }

    public static ItemStack createSkullItem(String itemName, short damage, String playerName) {
        ItemStack toReturn = new ItemStack(Material.SKULL_ITEM, 1, damage);
        SkullMeta itemMeta = (SkullMeta) toReturn.getItemMeta();
        itemMeta.setOwner(playerName);
        itemMeta.setDisplayName(C.translate(itemName));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createSkullItem(String itemName, short damage, String playerName, List<String> lore) {
        ItemStack toReturn = new ItemStack(Material.SKULL_ITEM, 1, damage);
        SkullMeta itemMeta = (SkullMeta) toReturn.getItemMeta();
        itemMeta.setOwner(playerName);
        List<String> loreList = new ArrayList(C.translate(lore));
        itemMeta.setLore(C.translate(loreList));
        itemMeta.setDisplayName(C.translate(C.translate(itemName)));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createSkullItem(String itemName, int amount, short damage, String playerName, List<String> lore) {
        ItemStack toReturn = new ItemStack(Material.SKULL_ITEM, amount, damage);
        SkullMeta itemMeta = (SkullMeta) toReturn.getItemMeta();
        itemMeta.setOwner(playerName);
        List<String> loreList = new ArrayList(lore);
        itemMeta.setLore(loreList);
        itemMeta.setDisplayName(C.translate(itemName));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, int amount) {
        ItemStack toReturn = new ItemStack(materialType, amount);
        ItemMeta itemMeta = toReturn.getItemMeta();
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName) {
        ItemStack toReturn = new ItemStack(materialType);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, short damage) {
        ItemStack toReturn = new ItemStack(materialType, 1, damage);
        ItemMeta itemMeta = toReturn.getItemMeta();
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, short damage) {
        ItemStack toReturn = new ItemStack(materialType, 1, damage);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, int amount) {
        ItemStack toReturn = new ItemStack(materialType, amount);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, int amount, short damage) {
        ItemStack toReturn = new ItemStack(materialType, amount, damage);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, int amount, short damage, List<String> lore) {
        ItemStack toReturn = new ItemStack(materialType, amount, damage);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        itemMeta.setLore(lore);
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType) {
        ItemStack toReturn = new ItemStack(materialType);
        ItemMeta itemMeta = toReturn.getItemMeta();
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, short damage, String lore) {
        ItemStack toReturn = new ItemStack(materialType, 1, damage);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        itemMeta.setLore(Collections.singletonList(lore));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, List<String> lore) {
        ItemStack toReturn = new ItemStack(materialType);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        itemMeta.setLore(lore);
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, String lore) {
        ItemStack toReturn = new ItemStack(materialType);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        itemMeta.setLore(Collections.singletonList(lore));
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, short damage, List<String> lore) {
        ItemStack toReturn = new ItemStack(materialType, 1, damage);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        itemMeta.setLore(lore);
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static ItemStack createItem(Material materialType, String itemName, short damage, Enchantment enchantment) {
        ItemStack toReturn = new ItemStack(materialType, 1, damage);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        itemMeta.addEnchant(enchantment, 10, true);
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    public static <T> ItemStack createItem(Material materialType, String itemName, Enchantment enchantment, List<String> lore) {
        ItemStack toReturn = new ItemStack(materialType, 1);
        ItemMeta itemMeta = toReturn.getItemMeta();
        itemMeta.setDisplayName(C.translate(itemName));
        itemMeta.addEnchant(enchantment, 10, true);
        itemMeta.setLore(lore);
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }
}
