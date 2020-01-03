package com.climpy.utils;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ClickableUtils {
  private List<TextComponent> components = new ArrayList<>();
  
  public ClickableUtils(String message) {
    TextComponent textComponent = new TextComponent(message);
    this.components.add(textComponent);
  }
  
  public ClickableUtils(String message, String hoverMessage, String clickString) {
    add(message, hoverMessage, clickString);
  }
  
  public TextComponent add(String message, String hoverMessage, String clickString) {
    TextComponent textComponent = new TextComponent(message);
    if (hoverMessage != null)
      textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(hoverMessage)).create())); 
    if (clickString != null)
      textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickString)); 
    this.components.add(textComponent);
    return textComponent;
  }
  
  public void add(String message) {
    this.components.add(new TextComponent(message));
  }
  
  public void sendToPlayer(Player player) {
    player.sendMessage(String.valueOf((BaseComponent[])asComponents()));
  }
  
  public TextComponent[] asComponents() {
    return this.components.<TextComponent>toArray(new TextComponent[0]);
  }
  
  public ClickableUtils() {}
}
