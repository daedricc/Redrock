package com.climpy.redrock.listener;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataPublisher;
import com.climpy.redrock.redis.messages.StaffSubscriber;
import com.climpy.redrock.server.ServerType;
import com.climpy.redrock.server.provider.DefaultServerProvider;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.redrock.utils.C;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RedrockListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        Profile user = TorchPlugin.getInstance().getProfileManager().getProfile(player.getName());
        ServerType serverType = RedrockPlugin.getInstance().getServerType();
        player.sendMessage(C.translate("&aGiriş yapılan sunucu: &f" + serverType.getInternalName()));

        if(serverType == ServerType.TEST_SERVER) {
            player.sendMessage(" ");
            player.sendMessage(C.translate("&c&lUyarı! &cŞu anda girmiş olduğunuz sunucu &a'Test Sunucusu' &cdurumundadır."));
            player.sendMessage(C.translate("&6Bu sunucudaki hiçbir eklenti stabil olmamakla beraber hesabınızdaki verilerde sorunlara yol açabilir."));
            player.sendMessage(" ");
        }


        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        if (user.getRank().hasPermission("torch.staff")) {
            DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
            JsonObject staffObject = new JsonObject();
            StaffSubscriber.StaffEventType staffEventType = StaffSubscriber.StaffEventType.JOIN;
            staffObject.addProperty("type", staffEventType.getInternalName());
            staffObject.addProperty("uuid", user.getUniqueUUID().toString());
            staffObject.addProperty("serverName", defaultServerProvider.getServerName());
            dataPublisher.write("staff_join_leave", staffObject);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        Profile user = TorchPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();

        if (user.getRank().hasPermission("torch.staff")) {
            DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
            JsonObject staffObject = new JsonObject();
            StaffSubscriber.StaffEventType staffEventType = StaffSubscriber.StaffEventType.QUIT;
            staffObject.addProperty("type", staffEventType.getInternalName());
            staffObject.addProperty("uuid", user.getUniqueUUID().toString());
            staffObject.addProperty("serverName", defaultServerProvider.getServerName());
            dataPublisher.write("staff_join_leave", staffObject);
        }
    }
}
