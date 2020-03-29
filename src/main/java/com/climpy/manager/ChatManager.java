package com.climpy.manager;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ChatManager {
    private final RedrockPlugin redrockPlugin;
    private Map<UUID, String> messageHistory;
    private Map<Integer, String> swearWords;
    private Map<Integer, String> blockedCommands;

    private boolean muteChat = false;

    private long defaultMuteChatTime = 600000L, defaultSlowChatTime = 5000L;
    private long muteChatTime;
    private long slowChatTime;

    public ChatManager(RedrockPlugin redrockPlugin) {
        this.redrockPlugin = redrockPlugin;
        this.swearWords = new HashMap<>();
        this.blockedCommands = new HashMap<>();
        this.messageHistory = (new MapMaker()).makeMap();
    }

    public void sendPrivateMessage(UUID senderUUID, UUID targetUUID, String privateMessage) {
        Profile sendingUser = RedrockPlugin.getInstance().getProfileManager().getProfile(senderUUID);
        Profile recipientUser = RedrockPlugin.getInstance().getProfileManager().getProfile(targetUUID);
        Player senderPlayer = Bukkit.getPlayer(sendingUser.getUniqueUUID());
        Player recipientPlayer = Bukkit.getPlayer(recipientUser.getUniqueUUID());
        if (!recipientUser.isMessagesVisible()) {
            senderPlayer.sendMessage(recipientUser.getRankType().getColor() + recipientUser.getName() + C.RED + " özel mesajları engelliyor.");
            return;
        }

        RankType sendingRank = sendingUser.getRankType();
        RankType recipientRank = recipientUser.getRankType();

        senderPlayer.sendMessage(getPrivateMessageTemplate(true, getDisplayNameTemplate(sendingUser, sendingRank), getDisplayNameTemplate(recipientUser, recipientRank), privateMessage));
        recipientPlayer.sendMessage(getPrivateMessageTemplate(false, getDisplayNameTemplate(sendingUser, sendingRank), getDisplayNameTemplate(recipientUser, recipientRank), privateMessage));

        recipientUser.setLastRepliedPlayer(sendingUser.getUniqueUUID());
        sendingUser.setLastRepliedPlayer(recipientUser.getUniqueUUID());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile onlineUser = RedrockPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
            if (onlineUser.getRankType().isAboveOrEqual(RankType.CHAT_MOD) && onlineUser.isSocialSpy())
                onlinePlayer.sendMessage(getSpyChatTemplate(getDisplayNameTemplate(sendingUser, sendingRank), getDisplayNameTemplate(recipientUser, recipientRank), privateMessage));
        }

        if (recipientUser.isMessagingSounds())
            recipientPlayer.playSound(recipientPlayer.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
    }


    public void setMuteChat(boolean newMuteChatStatus, long newMuteChatTime) {
        setMuteChat(newMuteChatStatus);
        if (newMuteChatStatus && newMuteChatTime != this.defaultMuteChatTime) {
            setMuteChatTime(System.currentTimeMillis() + newMuteChatTime);
            return;
        }
        setMuteChatTime(System.currentTimeMillis() + this.defaultMuteChatTime);
    }

    private String getDisplayNameTemplate(Profile user, RankType rankType) {
        return !rankType.isAboveOrEqual(RankType.VIP) ? (C.WHITE + user.getName() + ChatColor.RESET) : (rankType
                .getColor() + user.getName() + ChatColor.RESET);
    }

    private String getPrivateMessageTemplate(boolean senderPlayer, String senderDisplayName, String recipientDisplayName, String privateMessage) {
        return ChatColor.GRAY + "(" + (senderPlayer ? "Alıcı " : "Gönderen ") + ChatColor.RESET + (senderPlayer ? recipientDisplayName : senderDisplayName) + ChatColor.GRAY + ") " + privateMessage;
    }

    private String getSpyChatTemplate(String senderDisplayName, String recipientDisplayName, String privateMessage) {
        return ChatColor.RED + "[Casus Sohbet] " + ChatColor.RESET + senderDisplayName + ChatColor.GRAY + " -> " + ChatColor.RESET + recipientDisplayName + ChatColor.WHITE + ": " + ChatColor.RESET + privateMessage;
    }

    public static List<String> BLOCKED_WORDS = ImmutableList.of("aw", "aq", "oç", "fuck", "motherfucker", "son of a bitch", "dildo", "orospu", "amcık", "piç", "yarrak", "nigger", "niger", "dick", "pussy", "vape.gg", "bitch", "kurva", "yarak", "salak", "idiot", "pornhub", "brazzers", "xnxx", "xvideos", "redtube", "killyourself", "amın feryadı", "pezenevk", "pezevenk ", "ibne", "yavşak", "kahpe", "orospuçocuğu", "oç", "anal",
            "sikik", "sikim", "taşağım", "taşaksız", "taşak", "sik", "aptal", "gerizekalı", "angut", "fahişe ", "sıfatını siktiğimin", "göt", "götünü sikeyim", "amhoşafı", "döl", "amcık", "ceddini sikeyim", "skerim", "skiim", "orrospu", "orospu çocuğu", "porno", "porn", "amk");
}
