package com.climpy.utils.menu.pagination;

import com.climpy.utils.menu.Button;
import com.climpy.utils.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedMenu extends Menu {
    private int page;

    public PaginatedMenu() {
        this.page = 1;
        setUpdateAfterClick(false);
    }

    public String getTitle(Player player) {
        return getPrePaginatedTitle(player);
    }

    public void modPage(Player player, int mod) {
        this.page += mod;
        getButtons().clear();
        openMenu(player);
    }

    public int getPages(Player player) {
        int buttonAmount = getAllPagesButtons(player).size();
        if (buttonAmount == 0)
            return 1;
        return (int)Math.ceil((buttonAmount / getMaxItemsPerPage(player)));
    }

    public Map<Integer, Button> getButtons(Player player) {
        int minIndex = (this.page - 1) * getMaxItemsPerPage(player);
        int maxIndex = this.page * getMaxItemsPerPage(player);
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(Integer.valueOf(0), new PageButton(-1, this));
        buttons.put(Integer.valueOf(8), new PageButton(1, this));
        for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
            int ind = ((Integer)entry.getKey()).intValue();
            if (ind >= minIndex && ind < maxIndex) {
                ind -= getMaxItemsPerPage(player) * (this.page - 1) - 9;
                buttons.put(Integer.valueOf(ind), entry.getValue());
            }
        }
        Map<Integer, Button> global = getGlobalButtons(player);
        if (global != null)
            for (Map.Entry<Integer, Button> gent : global.entrySet())
                buttons.put(gent.getKey(), gent.getValue());
        return buttons;
    }

    public int getMaxItemsPerPage(Player player) {
        return 18;
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    public abstract String getPrePaginatedTitle(Player paramPlayer);

    public abstract Map<Integer, Button> getAllPagesButtons(Player paramPlayer);

    public int getPage() {
        return this.page;
    }
}
