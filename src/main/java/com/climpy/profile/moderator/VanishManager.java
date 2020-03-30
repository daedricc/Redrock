package com.climpy.profile.moderator;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.moderator.items.extnds.VanishChangeItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class VanishManager {
    private List<UUID> vanished;

    public VanishManager() {
        this.vanished = new ArrayList<>();
    }

    public void setVanishStatus(UUID uniqueUUID, boolean active) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(uniqueUUID);

        if (profile.toPlayer().hasMetadata("staffmode")) {
            StaffManager staffManager = RedrockPlugin.getInstance().getStaffManager();

            VanishChangeItem vanishChangeItem = (VanishChangeItem) staffManager.getStaffModeItem(VanishChangeItem.class);
            vanishChangeItem.changeVanishItem(profile.isVanishStatus()).applyToPlayer(profile.toPlayer());
        }

        if(active && !this.vanished.contains(uniqueUUID)) {
            this.vanished.add(uniqueUUID);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Profile onlineProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
                if (onlineProfile == null || onlinePlayer == profile.toPlayer()) {
                    continue;
                }

              //TODO: Bak buraya:  if (onlineProfile.getActiveRank().isAboveOrEqual(profile.getActiveRank())) {
                    onlinePlayer.hidePlayer(profile.toPlayer());
              //  }
            }

            profile.toPlayer().setMetadata("vanished", new FixedMetadataValue(RedrockPlugin.getInstance(), true));

            profile.setVanishStatus(true);
            profile.saveProfile();
            return;
        }

        profile.toPlayer().removeMetadata("vanished", RedrockPlugin.getInstance());
        this.vanished.remove(uniqueUUID);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (profile.toPlayer().canSee(onlinePlayer)) {
                continue;
            }

            onlinePlayer.showPlayer(profile.toPlayer());
        }

        profile.setVanishStatus(false);
        profile.saveProfile();
    }
}
