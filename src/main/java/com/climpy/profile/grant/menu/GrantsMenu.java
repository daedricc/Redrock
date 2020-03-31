package com.climpy.profile.grant.menu;

import com.climpy.inv.ClickableItem;
import com.climpy.inv.SmartInventory;
import com.climpy.inv.content.InventoryContents;
import com.climpy.inv.content.InventoryProvider;
import com.climpy.inv.content.Pagination;
import com.climpy.profile.Profile;
import com.climpy.profile.grant.Grant;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.history.menu.HistoryCategoryMenu;
import com.climpy.utils.C;
import com.climpy.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GrantsMenu implements InventoryProvider {
    private Profile profile;

    public GrantsMenu(Profile profile) {
        this.profile = profile;
    }

    public static SmartInventory getInventory(Profile profile) {
        return SmartInventory.builder()
                .provider(new GrantsMenu(profile))
                .size(1, 9)
                .title("&8Lütfen, ceza türü seçiniz.")
                .build();
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        contents.set(2, 3, ClickableItem.of(ItemUtils.createSkullItem("§aÖnceki Sayfa", (short) 3, "MHF_ArrowLeft", Arrays.asList("", "")),
                e -> getInventory(profile).open(player, pagination.previous().getPage())));

        contents.set(2, 5, ClickableItem.of(ItemUtils.createSkullItem("§aSonraki Sayfa", (short) 3, "MHF_ArrowRight", Arrays.asList("", "")),
                e -> getInventory(profile).open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        List<Grant> grants = new ArrayList<>();
        //grants = Collections.unmodifiableList(profile.getGrants().stream().filter(grant -> grant.getUuid() == profile.getUniqueUUID()).collect(Collectors.toList()));

        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[grants.size()];

        for (int i = 0; i < items.length; i++) {
            Grant grant = grants.get(i);

            items[i] = ClickableItem.of(ItemUtils.createItem(Material.WOOL, C.GRAY + "- ID: " + grant.getRank(), 1, (short) (grant.isRemoved() ? 13 : 14), Arrays.asList(
                    C.MENU_BAR,
                    C.YELLOW + "Kullanıcı: " + C.RED + profile.getName(),
                    C.YELLOW + "Ekleyen: " + C.RED + grant.getAddedBy(),
                    C.YELLOW + "Nedeni: " + C.RED + grant.getAddedReason(),
                    C.YELLOW + "Eklenme Zamanı: " + C.RED + grant.getAddedAt(),
                    C.YELLOW + "Kalan Süre: " + C.RED + (grant.isPermanent() ? "Sonsuza dek! " : grant.getDuration()),
                    C.MENU_BAR
            )), e -> {

            });
        }

    }
}
