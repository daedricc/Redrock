package com.climpy.utils.menu.pagination;

import com.climpy.utils.menu.Button;
import com.climpy.utils.menu.Menu;
import com.climpy.utils.menu.button.BackButton;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ViewAllPagesMenu extends Menu {
    @NonNull
    private PaginatedMenu menu;

    public String getTitle(Player player) {
        return "Sayfaya Atla";
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(Integer.valueOf(0), new BackButton(this.menu));
        int index = 10;
        for (int i = 1; i <= this.menu.getPages(player); i++) {
            buttons.put(Integer.valueOf(index++), new JumpToPageButton(i, this.menu, (this.menu.getPage() == i)));
            if ((index - 8) % 9 == 0)
                index += 2;
        }
        return buttons;
    }

    public boolean isAutoUpdate() {
        return true;
    }

    public ViewAllPagesMenu(@NonNull PaginatedMenu menu) {
        if (menu == null)
            throw new NullPointerException("menu is marked @NonNull but is null");
        this.menu = menu;
    }

    @NonNull
    public PaginatedMenu getMenu() {
        return this.menu;
    }
}
