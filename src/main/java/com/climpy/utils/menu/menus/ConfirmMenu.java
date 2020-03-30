package com.climpy.utils.menu.menus;

import java.util.HashMap;
import java.util.Map;

import com.climpy.utils.menu.Button;
import com.climpy.utils.menu.Menu;
import com.climpy.utils.menu.button.ConfirmationButton;
import com.climpy.utils.menu.callback.TypeCallback;
import org.bukkit.entity.Player;

public class ConfirmMenu extends Menu {
  private String title;
  
  private TypeCallback<Boolean> response;
  
  private boolean closeAfterResponse;
  
  private Button[] centerButtons;
  
  public ConfirmMenu(String title, TypeCallback<Boolean> response, boolean closeAfter, Button... centerButtons) {
    this.title = title;
    this.response = response;
    this.closeAfterResponse = closeAfter;
    this.centerButtons = centerButtons;
  }
  
  public Map<Integer, Button> getButtons(Player player) {
    HashMap<Integer, Button> buttons = new HashMap<>();
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        buttons.put(Integer.valueOf(getSlot(x, y)), new ConfirmationButton(true, this.response, this.closeAfterResponse));
        buttons.put(Integer.valueOf(getSlot(8 - x, y)), new ConfirmationButton(false, this.response, this.closeAfterResponse));
      } 
    } 
    if (this.centerButtons != null)
      for (int i = 0; i < this.centerButtons.length; i++) {
        if (this.centerButtons[i] != null)
          buttons.put(Integer.valueOf(getSlot(4, i)), this.centerButtons[i]); 
      }  
    return buttons;
  }
  
  public String getTitle(Player player) {
    return this.title;
  }
}
