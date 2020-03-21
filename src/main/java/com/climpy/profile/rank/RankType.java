package com.climpy.profile.rank;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@Getter @AllArgsConstructor
public enum RankType {
    OWNER("§4Owner §c", "", "owner", "§4Owner", ChatColor.DARK_RED, Arrays.asList("worldedit.*", "fawe.admin")),
    CO_OWNER("§4Co-Owner §c", "", "co_owner", "§4Co-Owner", ChatColor.DARK_RED, Arrays.asList("worldedit.*", "fawe.admin")),
    ADMIN("§4Admin §c", "", "admin", "§4Admin", ChatColor.DARK_RED, Arrays.asList("worldedit.*", "fawe.admin")),
    DEVELOPER("§9Developer §b", "", "developer", "§9Developer", ChatColor.BLUE, Arrays.asList("worldedit.*", "fawe.admin")),
    BUILDTEAMPLUS("§eMimar§c+ §6", "", "buildteamplus", "§eMimar+", ChatColor.YELLOW, Arrays.asList("worldedit.*", "fawe.admin")),
    BUILDTEAM("§eMimar §6", "", "buildteam", "§eMimar", ChatColor.YELLOW, Arrays.asList("worldedit.*", "fawe.admin")),
    MODPLUS("§5Mod§b+ §d", "", "modplus", "§5Mod+", ChatColor.DARK_PURPLE, Arrays.asList("worldedit.*", "fawe.admin")),
    MOD("§dMod §5", "", "mod", "§dMod", ChatColor.LIGHT_PURPLE, Arrays.asList("worldedit.*", "fawe.admin", "fawe.admin")),
    CHAT_MOD("§dChat Mod §e", "", "chat_mod", "§dChat Mod", ChatColor.YELLOW, Arrays.asList("worldedit.*", "fawe.admin")),
    HELPER("§3Yardımcı §3", "", "helper", "§3Yardımcı", ChatColor.BLUE, Collections.emptyList()),
    YOUTUBER("§cYoutuber §e", "", "youtuber", "§dYoutuber", ChatColor.YELLOW, Collections.emptyList()),
    CVIPPLUSPLUS("§eCVIP§c++ §6", "", "cvipplusplus", "§eCVIP§c++", ChatColor.YELLOW, Collections.emptyList()),
    CVIPPLUS("§eCVIP§c+ §6", "", "cvipplus", "§eCVIP§c+", ChatColor.YELLOW, Collections.emptyList()),
    CVIP("§eCVIP §6", "", "cvip", "§eCVIP", ChatColor.YELLOW, Collections.emptyList()),
    MVIPPLUS("§dMVIP§e+ §d", "", "mvipplus", "§dMVIP§e+", ChatColor.LIGHT_PURPLE, Collections.emptyList()),
    MVIP("§dMVIP §d", "", "mvip", "§dMVIP", ChatColor.LIGHT_PURPLE, Collections.emptyList()),
    VIPPLUS("§bVIP§6+§b §b", "", "vipplus", "§bVIP§6+", ChatColor.AQUA, Collections.emptyList()),
    VIP("§bVIP §b", "", "vip", "§bVIP", ChatColor.AQUA, Collections.emptyList()),
    DEFAULT("§9", "", "default", "§7Normal", ChatColor.GRAY, Collections.emptyList());

    private String prefix, suffix, rankName, displayName;
    private ChatColor color;
    private List<String> permissions = new LinkedList<>();

    public boolean isAboveOrEqual(RankType rank) {
        return (ordinal() <= rank.ordinal());
    }

    public static RankType getRankOrDefault(String internalName) {
        RankType rankType;

        try {
            rankType = RankType.valueOf(internalName.toUpperCase(Locale.ENGLISH));
        } catch (Exception ex) {
            rankType = RankType.DEFAULT;
        }

        return rankType;
    }

    public static String separatorCheck(StringBuilder stringBuilder) {
        String STRING_SEPARATOR = "\n";
        return separatorCheck(stringBuilder, "\n");
    }

    public static String separatorCheck(StringBuilder stringBuilder, String STRING_SEPARATOR) {
        String toReturn = stringBuilder.toString();
        if (toReturn.endsWith(STRING_SEPARATOR)) {
            toReturn = toReturn.substring(0, toReturn.length() - STRING_SEPARATOR.length());
        }

        return toReturn;
    }

    public static String getAllRanks() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RankType rankType : values()) {
            stringBuilder.append(rankType.getColor()).append(rankType.color).append(rankType.getRankName());
            stringBuilder.append(ChatColor.WHITE).append(", ");
        }

        if (stringBuilder.length() == 0) {
            return "Sunucu üzerinde rütbe grubu yok.";
        }

        return separatorCheck(stringBuilder, ", ");
    }

    public List<String> getPlayerAllPermissions(Player player) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        List<String> permissions = new ArrayList<>();
        permissions.addAll(this.permissions);
        permissions.addAll(user.getRankType().getPermissions());

        return permissions;
    }
}
