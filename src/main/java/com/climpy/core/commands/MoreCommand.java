package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(user.getUniqueUUID());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
                return true;
            }

            ItemStack itemStack = player.getItemInHand();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                sender.sendMessage(C.RED + "Elinizde herhangi bir eşya bulunmamktadır.");
                return true;
            }

            if (itemStack.getAmount() == itemStack.getMaxStackSize()) {
                sender.sendMessage(C.RED + "Elinizde ki eşya zaten maksimum miktarda. (" + itemStack.getMaxStackSize() + ")");
                return true;
            }


            player.getItemInHand().setAmount(64);
            player.updateInventory();
            player.sendMessage(C.GREEN + "Elinizdeki eşya " + C.WHITE + "maksimum" + C.GREEN + " hale getirildi.");
            return true;
        }
        sender.sendMessage("Bu islem sadece oyundan yurutelebilir.");
        return true;
    }
}
