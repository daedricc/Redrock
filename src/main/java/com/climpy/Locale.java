package com.climpy;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

import java.text.MessageFormat;

@AllArgsConstructor
public enum Locale {
    PLAYER_NOT_FOUND("§cBu isimde bir oyuncu bulunamadı."),
    PLAYER_OFFLINE("§cOyuncu sunucu üzerinde açık değil.");

    private String msg;

    public static String format(Locale locale) {
        return locale.msg;
    }
}
