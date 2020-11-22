package com.climpy.redrock.report.menu;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.inventory.ClickableItem;
import com.climpy.redrock.inventory.SmartInventory;
import com.climpy.redrock.inventory.content.InventoryContents;
import com.climpy.redrock.inventory.content.InventoryProvider;
import com.climpy.redrock.inventory.content.Pagination;
import com.climpy.redrock.inventory.content.SlotIterator;
import com.climpy.redrock.report.ReportData;
import com.climpy.redrock.report.ReportManager;
import com.climpy.redrock.report.ReportStatus;
import com.climpy.torch.profile.Profile;
import com.climpy.torch.profile.ProfileManager;
import com.climpy.redrock.utils.C;
import com.climpy.redrock.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ReportProfileMenu implements InventoryProvider {

    private ReportManager reportData;
    private Profile profile;

    public ReportProfileMenu(ReportManager reportManager, Profile profile) {
        this.reportData = reportManager;
        this.profile = profile;
    }

    public static SmartInventory getInventory(ReportManager reportManager, Profile profile) {
        return SmartInventory.builder()
                .provider(new ReportProfileMenu(reportManager, profile))
                .size(3, 9)
                .title(profile.getName() + " raporları: ")
                .build();
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        ReportManager reportManager = RedrockPlugin.getInstance().getReportManager();
        //List<ReportData> reports;

        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[reportData.getReports(profile.getUniqueUUID()).size()];


        for (int i = 0; i < items.length; i++) {
            ReportData reportData = reportManager.getReports(profile.getUniqueUUID()).get(i);

            items[i] = ClickableItem.empty(ItemUtils.createItem(Material.WOOL, C.GRAY + "- ID: " + reportData.getReportID(), 1, (short) (reportData.getReportStatus() == ReportStatus.ACTIVE ? 13 : 14), Arrays.asList(
                    C.MENU_BAR,
                    C.YELLOW + "Raporlanan: " + C.RED + new ProfileManager().getProfile(reportData.getUniqueUUID()).getName(),
                    C.YELLOW + "Raporlayan: " + C.RED + new ProfileManager().getProfile(reportData.getAddedBy()).getName(),
                    C.YELLOW + "Neden: " + C.RED + reportData.getReason(),
                    C.YELLOW + "Durum: " + C.RED + (reportData.getReportStatus() == ReportStatus.ACTIVE ? C.GREEN + "Açık" : C.RED + "Kapalı"),
                    C.MENU_BAR
            )));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(2, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> ReportProfileMenu.getInventory(reportManager, profile).open(player, pagination.previous().getPage())));
        contents.set(2, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> ReportProfileMenu.getInventory(reportManager, profile).open(player, pagination.next().getPage())));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
