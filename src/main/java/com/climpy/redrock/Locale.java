package com.climpy.redrock;

import com.climpy.redrock.utils.C;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum Locale {
    TURKISH(""),
    ENGLISH(""),

    PLAYER_NOT_FOUND("&cBu isimde bir oyuncu veritabanında bulunamadı."),
    PLAYER_OFFLINE("&cOyuncu sunucu üzerinde açık değil."),
    NO_PERM(C.DARK_RED + C.UNICODE_CAUTION + " &cYetkiye sahip değilsin.");

    private String msg;

    public static String format(Locale locale) {
        return ChatColor.translateAlternateColorCodes('&', locale.msg);
    }
}
