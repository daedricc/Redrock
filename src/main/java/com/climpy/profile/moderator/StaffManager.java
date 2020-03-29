package com.climpy.profile.moderator;

import com.climpy.RedrockPlugin;
import com.climpy.config.FileConfig;
import com.climpy.profile.Profile;
import com.climpy.profile.moderator.items.StaffModeItem;
import com.climpy.profile.moderator.items.extnds.*;
import com.climpy.utils.ConfigUtils;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

@Getter
public class StaffManager {
    private List<UUID> staffs;
    private Map<UUID, ItemStack[]> savedContents, savedArmor;
    private Map<Class<? extends StaffModeItem>, StaffModeItem> staffModeItems;

    public StaffManager() {
        this.staffs = new ArrayList<>();
        this.savedContents = new HashMap<>();
        this.savedArmor = new HashMap<>();
        this.staffModeItems = new HashMap<>();

        Arrays.asList(new CompassItem(), new InspectorItem(), new MinerTeleporterItem(), new OnlineStaffMenuItem(), new PlayerFrozenItem(), new RandomTeleporterItem(), new VanishChangeItem()).forEach(staffModeItems -> {
            this.staffModeItems.put(staffModeItems.getClass(), staffModeItems);
        });
    }

    public void setStaffMode(UUID uniqueUUID, boolean active) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(uniqueUUID);
        if(active && !this.staffs.contains(uniqueUUID)) {
            this.staffs.add(uniqueUUID);

            this.savePlayerInventory(uniqueUUID, profile.toPlayer().getInventory());
            profile.toPlayer().getInventory().clear();

            this.staffModeItems.values().forEach(staffModeItem -> {
                if(staffModeItem instanceof VanishChangeItem) {
                    VanishChangeItem vanishChangeItem = (VanishChangeItem) staffModeItem;
                    vanishChangeItem.changeVanishItem(profile.isVanishStatus());
                }

                staffModeItem.applyToPlayer(profile.toPlayer());
            });

            VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
            vanishManager.setVanishStatus(uniqueUUID, true);

            profile.toPlayer().setMetadata("staffmode", new FixedMetadataValue(RedrockPlugin.getInstance(), true));

            profile.setStaffMode(true);
            profile.saveProfile();

            if (profile.toPlayer().getGameMode() != GameMode.CREATIVE) {
                profile.toPlayer().setGameMode(GameMode.CREATIVE);
                profile.toPlayer().setAllowFlight(true);
                profile.toPlayer().setFlying(true);
            }
            return;
        }

        this.staffs.remove(uniqueUUID);
        this.staffModeItems.values().forEach(staffModeItem -> {
            staffModeItem.removeToPlayer(profile.toPlayer());
        });

        profile.toPlayer().getInventory().clear();
        this.loadPlayerInventory(uniqueUUID, profile.toPlayer().getInventory());

        profile.toPlayer().removeMetadata("staffmode", RedrockPlugin.getInstance());

        VanishManager vanishManager = RedrockPlugin.getInstance().getVanishManager();
        vanishManager.setVanishStatus(uniqueUUID, false);

        profile.setStaffMode(false);
        profile.saveProfile();

        if (profile.toPlayer().getGameMode() != GameMode.SURVIVAL) {
            profile.toPlayer().setGameMode(GameMode.SURVIVAL);
            profile.toPlayer().setAllowFlight(false);
            profile.toPlayer().setFlying(false);
        }
    }

    public void savePlayerInventory(UUID targetUUID, PlayerInventory targetInventory) {
        this.savedContents.put(targetUUID, targetInventory.getContents());
        this.savedArmor.put(targetUUID, targetInventory.getArmorContents());

        FileConfig inventoryFile = new FileConfig(RedrockPlugin.getInstance(), "./staffs/" + targetUUID.toString() + ".yml");
        ConfigUtils inventoryUtils = new ConfigUtils(inventoryFile, "items");

        inventoryUtils.set("inventories", this.savedContents.get(targetUUID));
        inventoryUtils.set("armors", this.savedArmor.get(targetUUID));
        inventoryUtils.configSave();
    }

    public void loadPlayerInventory(UUID targetUUID, PlayerInventory targetInventory) {
        FileConfig inventoryFile = new FileConfig(RedrockPlugin.getInstance(), "./staffs/" + targetUUID.toString() + ".yml");
        ConfigUtils inventoryUtils = new ConfigUtils(inventoryFile, "items");

        if (this.savedContents.containsKey(targetUUID) || this.savedArmor.containsKey(targetUUID)) {
            ItemStack[] inventoryContents = this.savedContents.get(targetUUID);
            ItemStack[] armorContents = this.savedArmor.get(targetUUID);

            if(inventoryContents.length > 0 || armorContents.length > 0) {
                targetInventory.setContents(inventoryContents);
                targetInventory.setArmorContents(armorContents);
            }

            if (inventoryUtils.get("inventories") != null || inventoryUtils.get("armors") != null) {
                inventoryUtils.configDelete();
            }

            this.savedContents.remove(targetUUID);
            this.savedArmor.remove(targetUUID);
        } else {
            if (inventoryUtils.get("inventories") == null || inventoryUtils.get("armors") == null) {
                return;
            }

            targetInventory.setContents((ItemStack[]) inventoryUtils.get("inventories"));
            targetInventory.setArmorContents((ItemStack[]) inventoryUtils.get("armors"));
            inventoryUtils.configDelete();
        }
    }

    public boolean hasStaffModeItem(ItemStack itemStack) {
        return this.staffModeItems.containsKey(itemStack.getClass());
    }

    public StaffModeItem getStaffModeItem(Class<?> itemStack) {
        return this.staffModeItems.get(itemStack);
    }

}
