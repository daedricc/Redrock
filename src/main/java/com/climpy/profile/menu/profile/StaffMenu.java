package com.climpy.profile.menu.profile;

import com.climpy.RedrockPlugin;
import com.climpy.inv.ClickableItem;
import com.climpy.inv.SmartInventory;
import com.climpy.inv.content.InventoryContents;
import com.climpy.inv.content.InventoryProvider;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import com.climpy.utils.ItemUtils;
import com.climpy.utils.menu.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("StaffMenu")
            .provider(new StaffMenu())
            .size(5, 9)
            .title("§8§l» §8Personel")
            .build();


    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
      //  contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name("&6._.").lore("&7.-.").build()));

        contents.set(1, 4, ClickableItem.of(ItemUtils.createSkullItem("&a&lYönetici Profili", (short) 3, player.getName(), Arrays.asList
                ("&fBilgileriniz aşağıda sıralandı.",
                        "",
                        "&7Yetki: " + user.getRankType().getDisplayName(),
                        "&7Staff Sohbeti: " + (user.isStaffChat() ? "&aAçık" : "&4Kapalı"),
                        "&7Staff Modu: " + (user.isStaffMode() ? "&aAçık" : "&4Kapalı"),
                "&7Staff Sohbeti Görünürlüğü: " + (user.isStaffChatVisible() ? "&aAçık" : "&4Kapalı"))), e -> {
        }));

        contents.set(3, 2, ClickableItem.of(new ItemBuilder(Material.NAME_TAG).name("&6&lStaff Sohbeti").lore(
                    new String[] {"&bMevcut Durum: " + (user.isStaffChat() ? "&aAçık" : "&cKapalı"), "", "&7" + (user.isStaffChat() ? "Kapatmak" : "Açmak") + " için tıkla."}).build(), e -> {
            user.setStaffChat(!user.isStaffChat());
            player.sendMessage(C.translate("&6Staff Sohbeti: " + (user.isStaffChat() ? "&aAçık" : "&cKapalı")));
            user.saveProfile();
        }));

        contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.DIAMOND_AXE).name("&6&lStaff Modu").lore(
                new String[] {"&bMevcut Durum: " + (user.isStaffMode() ? "&aAçık" : "&cKapalı"), "", "&7" + (user.isStaffMode() ? "Kapatmak" : "Açmak") + " için tıkla."}).build(), e -> {
            user.setStaffMode(!user.isStaffMode());
            player.sendMessage(C.translate("&6Staff Modu: " + (user.isStaffMode() ? "&aAçık" : "&cKapalı")));
            user.saveProfile();
        }));

        contents.set(3, 6, ClickableItem.of(new ItemBuilder(Material.ENDER_PEARL).name("&6&lStaff Sohbeti Görünürlüğü").lore(
                new String[] {"&bMevcut Durum: " + (user.isStaffChatVisible() ? "&aAçık" : "&cKapalı"), "", "&7" + (user.isStaffChatVisible() ? "Kapatmak" : "Açmak") + " için tıkla."}).build(), e -> {
            user.setStaffChatVisible(!user.isStaffChatVisible());
            player.sendMessage(C.translate("&6Staff Sohbeti Görünürlüğü: " + (user.isStaffChatVisible() ? "&aAçık" : "&cKapalı")));
            user.saveProfile();
        }));




    }
}
