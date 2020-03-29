package com.climpy.punishments.history.menu;

import com.climpy.inv.ClickableItem;
import com.climpy.inv.SmartInventory;
import com.climpy.inv.content.InventoryContents;
import com.climpy.inv.content.InventoryProvider;
import com.climpy.profile.Profile;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentType;
import com.climpy.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryCategoryMenu implements InventoryProvider {

    private Profile profile;

    public HistoryCategoryMenu(Profile profile) {
        this.profile = profile;
    }

    public static SmartInventory getInventory(Profile profile) {
        return SmartInventory.builder()
                .provider(new HistoryCategoryMenu(profile))
                .size(1, 9)
                .title("&8Lütfen, ceza türü seçiniz.")
                .build();
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        List<Punishment> banPunishments = Collections.unmodifiableList(profile.getPunishments().stream().filter(punishment -> punishment.getPunishmentType() == PunishmentType.BAN || punishment.getPunishmentType() == PunishmentType.IP_BAN).collect(Collectors.toList()));
        List<Punishment> kickPunishments = Collections.unmodifiableList(profile.getPunishments().stream().filter(punishment -> punishment.getPunishmentType() == PunishmentType.KICK).collect(Collectors.toList()));
        List<Punishment> blacklistPunishments = Collections.unmodifiableList(profile.getPunishments().stream().filter(punishment -> punishment.getPunishmentType() == PunishmentType.BLACKLIST).collect(Collectors.toList()));
        List<Punishment> mutePunishments = Collections.unmodifiableList(profile.getPunishments().stream().filter(punishment -> punishment.getPunishmentType() == PunishmentType.MUTE || punishment.getPunishmentType() == PunishmentType.IP_MUTE).collect(Collectors.toList()));
        List<Punishment> warnPunishments = Collections.unmodifiableList(profile.getPunishments().stream().filter(punishment -> punishment.getPunishmentType() == PunishmentType.WARN).collect(Collectors.toList()));

        contents.set(0, 0, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "&cYasaklanma", 1, (short) 14, (banPunishments.isEmpty() ?
                Arrays.asList(
                        "",
                        "§7Bu kullanıcın yasaklanma cezası bulunamadı."
                ) : Arrays.asList(
                "",
                "§7Bu kullanıcın kayıtlarda " + banPunishments.size() + " cezası var."
        ))), e -> {
            if (!banPunishments.isEmpty()) {
                HistoryPunishmentMenu.getInventory(PunishmentType.BAN, profile).open(player);
            }
        }));

        contents.set(0, 2, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "&7Atılama", 1, (short) 8, (kickPunishments.isEmpty() ?
                Arrays.asList(
                        "",
                        "§7Bu kullanıcın yasaklanma cezası bulunamadı."
                ) : Arrays.asList(
                "",
                "§7Bu kullanıcın kayıtlarda " + kickPunishments.size() + " cezası var."
        ))), e -> {
            if (!kickPunishments.isEmpty()) {
                HistoryPunishmentMenu.getInventory(PunishmentType.KICK, profile).open(player);
            }
        }));

        contents.set(0, 4, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "&4Karaliste", 1, (short) 14, (blacklistPunishments.isEmpty() ?
                Arrays.asList(
                        "",
                        "§7Bu kullanıcın yasaklanma cezası bulunamadı."
                ) : Arrays.asList(
                "",
                "§7Bu kullanıcın kayıtlarda " + blacklistPunishments.size() + " cezası var."
        ))), e -> {
            if (!blacklistPunishments.isEmpty()) {
                HistoryPunishmentMenu.getInventory(PunishmentType.BLACKLIST, profile).open(player);
            }
        }));

        contents.set(0, 6, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "&eSusturulma", 1, (short) 4, (mutePunishments.isEmpty() ?
                Arrays.asList(
                        "",
                        "§7Bu kullanıcın yasaklanma cezası bulunamadı."
                ) : Arrays.asList(
                "",
                "§7Bu kullanıcın kayıtlarda " + mutePunishments.size() + " cezası var."
        ))), e -> {
            if (!mutePunishments.isEmpty()) {
                HistoryPunishmentMenu.getInventory(PunishmentType.MUTE, profile).open(player);
            }
        }));


        contents.set(0, 8, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "&bUyarı", 1, (short) 9, (warnPunishments.isEmpty() ?
                Arrays.asList(
                        "",
                        "§7Bu kullanıcın yasaklanma cezası bulunamadı."
                ) : Arrays.asList(
                "",
                "§7Bu kullanıcın kayıtlarda " + warnPunishments.size() + " cezası var."
        ))), e -> {
            if (!warnPunishments.isEmpty()) {
                HistoryPunishmentMenu.getInventory(PunishmentType.WARN, profile).open(player);
            }
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
