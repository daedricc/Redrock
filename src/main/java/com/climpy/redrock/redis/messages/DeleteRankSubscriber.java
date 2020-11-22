package com.climpy.redrock.redis.messages;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataSubscriber;
import com.climpy.redrock.utils.C;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeleteRankSubscriber extends DataSubscriber {
    public DeleteRankSubscriber() {
        super(RedrockPlugin.getInstance(), "delete_rank");
    }

    public void handleMessage(JsonObject object) {
        String rank = object.get("rank").getAsString();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile onlineUser = TorchPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
            if (onlineUser.getRank().hasPermission("torch.staff")) {
                String stringBuilder = C.translate("&b[Redrock] &fA rank named &7" + rank + " &frank is deleted from network.");
                onlinePlayer.sendMessage(stringBuilder);
            }
        }
    }
}
