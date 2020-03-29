package com.climpy.profile.symbol;

import com.climpy.RedrockPlugin;
import com.climpy.inv.ClickableItem;
import com.climpy.inv.SmartInventory;
import com.climpy.inv.content.InventoryContents;
import com.climpy.inv.content.InventoryProvider;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Random;

public class SymbolMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("symbolMenu")
            .provider(new SymbolMenu())
            .size(5, 9)
            .title("§8§l» §8Sembol Menüsü")
            .build();

    private final Random random = new Random();

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {

        final Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        contents.set(0, 1, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §7Sembol: §f§l★", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_1.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.VIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_1) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_1);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(0, 2, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §7Sembol: §f§l✿", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_2.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.VIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_2) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_2);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(0, 3, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §f§l♫", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_3.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.VIPPLUS.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_3) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_3);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(0, 4, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §d§l❥", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_4.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.VIPPLUS.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_4) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_4);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(0, 5, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §d§l♚", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_5.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.MVIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_5) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_5);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(0, 6, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §d§l☘", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_6.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.MVIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_6) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_6);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(0, 7, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §a§l✎", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_7.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.MVIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_7) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_7);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(1, 2, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §a§l✦", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_8.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.MVIPPLUS.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_8) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_8);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(1, 3, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §a§l❦", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_9.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.MVIPPLUS.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_9) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_9);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(1, 4, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §a§lE", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+", " §7- §eCVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_10.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_10) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_10);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(1, 5, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§l❝E❞", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+", " §7- §eCVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_11.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_11) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_11);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(1, 6, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§lPRO", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+", " §7- §eCVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_12.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_12) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_12);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(2, 3, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§lMASTER", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+", " §7- §eCVIP")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_13.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_13) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_13);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(2, 4, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§lCRY", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+", " §7- §eCVIP", " §7- §eCVIP§c+")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_14.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.CVIPPLUS.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_14) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_14);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(2, 5, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§lUwU", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+", " §7- §eCVIP", " §7- §eCVIP§c+", " §7- §eCVIP§c++")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_15.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.CVIPPLUSPLUS.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_15) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_15);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(3, 4, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §d#CLIMPY", Arrays.asList("", "§7Kullanabilir:", " §7- §bVIP", " §7- §bVIP§6+", " §7- §dMVIP", " §7- §dMVIP§e+", " §7- §eCVIP", " §7- §eCVIP§c+", " §7- §eCVIP§c++")), e -> {
            if (!targetUser.getRankType().isAboveOrEqual(SymbolType.SYMBOL_16.getRankType())) {
                player.sendMessage(ChatColor.RED + "Bu sembolü kullanmak için " + RankType.CVIPPLUSPLUS.getDisplayName() + ChatColor.RED + " ve üzeri sembolü gerekir.");
                return;
            }

            if (targetUser.getSymbolType() == SymbolType.SYMBOL_16) {
                player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_16);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
            targetUser.saveProfile();
        }));

        contents.set(4, 8, ClickableItem.of(ItemUtils.createItem(Material.BARRIER, "§cSembolleri Sil", Arrays.asList("", "§7üstünüzdeki sembolleri siler.")), e -> {
            if (targetUser.getSymbolType() == SymbolType.SYMBOL_0) {
                player.sendMessage(ChatColor.RED + "Sembole sahip değilsiniz.");
                return;
            }

            targetUser.setSymbolType(SymbolType.SYMBOL_0);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSembol kaldırıldı."));
            targetUser.saveProfile();
        }));

    }
}
