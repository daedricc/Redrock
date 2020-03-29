package com.climpy.profile.moderator.menu;

import com.climpy.inv.ClickableItem;
import com.climpy.inv.SmartInventory;
import com.climpy.inv.content.InventoryContents;
import com.climpy.inv.content.InventoryProvider;
import com.climpy.inv.content.SlotPos;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import com.climpy.utils.DateTimeUtils;
import com.climpy.utils.ItemUtils;
import com.climpy.utils.TimeUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class InspectorMenu implements InventoryProvider {

    private Profile profile;

    public InspectorMenu(Profile profile) {
        this.profile = profile;
    }

    public static SmartInventory getInventory(Profile profile) {
        return SmartInventory.builder()
                .provider(new InspectorMenu(profile))
                .size(6, 9)
                .title("&8İnceleniyor: " + profile.getRankType().getColor() + profile.getName())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        getInventoryArmor(contents);
        getPlayerInfo(contents);

    }

    private void getInventory(InventoryContents contents) {

    }

    private void getInventoryArmor(InventoryContents contents) {
        contents.set(0, 0, ClickableItem.empty((profile.toPlayer().getInventory().getHelmet() != null
                ? ItemUtils.createItem(profile.toPlayer().getInventory().getHelmet().getType(), "&bKask", 1)
                : ItemUtils.createItem(Material.BARRIER, "&bKask: &fBoş", 1))));

        contents.set(1, 0, ClickableItem.empty((profile.toPlayer().getInventory().getChestplate() != null
                ? ItemUtils.createItem(profile.toPlayer().getInventory().getChestplate().getType(), "&bZırh", 1)
                : ItemUtils.createItem(Material.BARRIER, "&bZırh: &fBoş", 1))));

        contents.set(2, 0, ClickableItem.empty((profile.toPlayer().getInventory().getLeggings() != null
                ? ItemUtils.createItem(profile.toPlayer().getInventory().getLeggings().getType(), "&bPantolon", 1)
                : ItemUtils.createItem(Material.BARRIER, "&bPantolon: &fBoş", 1))));

        contents.set(3, 0, ClickableItem.empty((profile.toPlayer().getInventory().getBoots() != null
                ? ItemUtils.createItem(profile.toPlayer().getInventory().getBoots().getType(), "&bAyakkabı", 1)
                : ItemUtils.createItem(Material.BARRIER, "&bAyakkabı: &fBoş", 1))));
    }

    private void getPlayerInfo(InventoryContents contents) {
        contents.set(5, 0, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "", 1, DyeColor.BLACK.getData())));
        contents.set(5, 2, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "", 1, DyeColor.BLACK.getData())));
        contents.set(5, 4, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "", 1, DyeColor.BLACK.getData())));
        contents.set(5, 6, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "", 1, DyeColor.BLACK.getData())));
        contents.set(5, 8, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "", 1, DyeColor.BLACK.getData())));

        contents.set(5, 1, ClickableItem.of(ItemUtils.createItem(Material.PAPER, C.DARK_RED + "Envanteri Sil", 1), e-> {
            profile.toPlayer().getInventory().clear();
            profile.toPlayer().getInventory().setHelmet(null);
            profile.toPlayer().getInventory().setChestplate(null);
            profile.toPlayer().getInventory().setLeggings(null);
            profile.toPlayer().getInventory().setBoots(null);
        }));

        contents.set(5, 3, ClickableItem.empty(ItemUtils.createItem(Material.MELON, C.DARK_RED + "Açlık Durumu", profile.toPlayer().getFoodLevel())));

        List<String> effects = new ArrayList<>();
        for (PotionEffect potionEffect : profile.toPlayer().getActivePotionEffects()) {
            String effectName = potionEffect.getType().getName();
            int effectAmplifier = potionEffect.getAmplifier();
            effects.add(ChatColor.AQUA + "Efekt: " + C.GRAY + effectName.replace("_", " ") + " " + effectAmplifier);
            effects.add(ChatColor.YELLOW + " - Süre: " + ChatColor.WHITE + TimeUtils.getDurationWords(potionEffect.getDuration(), true, true));
        }
        contents.set(5, 5, ClickableItem.empty(ItemUtils.createItem(Material.POTION, "&bİksirler", 1, (short) 0, effects)));

        contents.set(5, 7, ClickableItem.empty(ItemUtils.createItem(Material.WATCH, C.DARK_RED + "IP: " + C.GRAY + profile.getCurrentAddress(), 1)));
    }
}