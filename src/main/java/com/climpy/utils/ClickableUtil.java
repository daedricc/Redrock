package com.climpy.utils;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClickableUtil {
  private List<TextComponent> components = new ArrayList<>();
  
  public ClickableUtil(String msg) {
    TextComponent message = new TextComponent(C.translate(msg));
    this.components.add(message);
  }
  
  public ClickableUtil(String msg, String hoverMsg, String clickString) {
    add(C.translate(msg), hoverMsg, clickString);
  }
  
  public TextComponent add(String msg, String hoverMsg, String clickString) {
    TextComponent message = new TextComponent(C.translate(msg));
    if (hoverMsg != null)
      message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(C.translate(hoverMsg))).create()));
    if (clickString != null)
      message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickString)); 
    this.components.add(message);
    return message;
  }
  
  public void add(String message) {
    this.components.add(new TextComponent(C.translate(message)));
  }
  
  public void sendToPlayer(CommandSender commandsender) {
    if (commandsender instanceof Player) {
      ((Player)commandsender).sendMessage(String.valueOf((BaseComponent[])asComponents()));
    } else {
      commandsender.sendMessage(TextComponent.toPlainText((BaseComponent[])asComponents()));
    } 
  }
  
  public TextComponent[] asComponents() {
    return this.components.<TextComponent>toArray(new TextComponent[0]);
  }
}
