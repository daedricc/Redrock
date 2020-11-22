package com.climpy.redrock.redis.messages;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataSubscriber;
import com.climpy.redrock.utils.C;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CreateRankSubscriber extends DataSubscriber {
    public CreateRankSubscriber() {
        super(RedrockPlugin.getInstance(), "create_rank");
    }

    public void handleMessage(JsonObject object) {
        String rank = object.get("rank").getAsString();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile onlineUser = TorchPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
            if (onlineUser.getRank().hasPermission("torch.staff")) {
                String stringBuilder = C.translate("&b[Redrock] &fA rank named " + rank + " &frank is created.");
                onlinePlayer.sendMessage(stringBuilder);
            }

        }
    }
}
