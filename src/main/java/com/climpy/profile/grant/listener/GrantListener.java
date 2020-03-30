package com.climpy.profile.grant.listener;

import com.climpy.RedrockPlugin;
import com.climpy.profile.grant.Grant;
import com.climpy.profile.grant.event.GrantAppliedEvent;
import com.climpy.profile.grant.event.GrantExpireEvent;
import com.climpy.profile.grant.procedure.GrantProcedure;
import com.climpy.profile.grant.procedure.GrantProcedureStage;
import com.climpy.profile.grant.procedure.GrantProcedureType;
import com.climpy.redis.data.DataPublisher;
import com.climpy.utils.C;
import com.climpy.utils.TimeUtil;
import com.climpy.utils.menu.Button;
import com.climpy.utils.menu.callback.TypeCallback;
import com.climpy.utils.menu.menus.ConfirmMenu;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sun.management.counter.perf.PerfLongArrayCounter;

@Getter @Setter
public class GrantListener implements Listener {
    private RedrockPlugin redrockPlugin;

    public GrantListener(RedrockPlugin redrockPlugin) {
        this.redrockPlugin = redrockPlugin;
    }

    @EventHandler
    public void onGrantAppliedEvent(GrantAppliedEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();
        player.sendMessage(C.translate("&r" + grant.getRank().getColoredName() + "&eyetkisi senin için &6" + ((grant.getDuration() == 2147483647L) ? "sonsuza dek " : TimeUtil.millisToRoundedTime(System.currentTimeMillis() - grant.getAddedAt() + grant.getDuration())) + " &everildi."));
    }

    @EventHandler
    public void onGrantExpireEvent(GrantExpireEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();
        player.sendMessage(C.translate("&eSenin &r" + grant.getRank().getColoredName() + "&e isimli yetkin zaman aşımına uğradı."));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission("redrock.admin"))
            return;
        final GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());
        if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                GrantProcedure.getProcedures().remove(procedure);
                player.sendMessage(C.RED + "Rütbe ekleme işlemini iptal ettiniz.");
                return;
            }
            if (procedure.getType() == GrantProcedureType.REMOVE)
                (new ConfirmMenu(String.valueOf(C.YELLOW) + "Rütbe silinsin mi ?", new TypeCallback<Boolean>() {
                    private static final long serialVersionUID = -988701755050783420L;

                    public void callback(Boolean data) {
                        if (data.booleanValue()) {
                            procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
                            procedure.getGrant().setRemovedAt(System.currentTimeMillis());
                            procedure.getGrant().setRemovedReason(event.getMessage());
                            procedure.getGrant().setRemoved(true);
                            procedure.finish();
                            if (procedure.getRecipient().toPlayer() != null) {
                           //     procedure.getRecipient().refreshDisplayName();
                                procedure.getRecipient().toPlayer().kickPlayer(C.translate("&cYou have removed the rank " + procedure.getGrant().getRank().getDisplayName() + ", please re-join the server."));
                            }
                            player.sendMessage(C.YELLOW + "The grant has been removed.");

                        //    GrantListener.this.plugin.getDatabaseManager().getRedisImpl().getRedis().sendPacket((Packet)new PacketDeleteGrant(procedure.getRecipient().getUuid(), procedure.getGrant()));

                            DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
                            JsonObject streamObject = new JsonObject();
                            streamObject.addProperty("uuid", procedure.getRecipient().getUniqueUUID().toString());
                            dataPublisher.write("delete_grant", streamObject);
                        } else {
                            procedure.cancel();
                            player.sendMessage(C.RED + "You did not confirm to remove the grant.");
                        }
                    }
                },true, new Button[0]) {
                public void onClose(Player player) {
                    if (!isClosedByMenu()) {
                        procedure.cancel();
                        player.sendMessage(C.RED + "&cYou did not confirm to remove the grant.");
                    }
                }
            }).openMenu(event.getPlayer());
        }
    }


}
