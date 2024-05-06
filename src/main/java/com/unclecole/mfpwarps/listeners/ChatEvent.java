package com.unclecole.mfpwarps.listeners;

import com.unclecole.mfpwarps.MFPWarps;
import com.unclecole.mfpwarps.database.WarpData;
import com.unclecole.mfpwarps.objects.Warp;
import com.unclecole.mfpwarps.utils.C;
import com.unclecole.mfpwarps.utils.PlaceHolder;
import com.unclecole.mfpwarps.utils.TL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatEvent implements Listener {

    private final int CHAR_LENGTH = 10;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent playerChatEvent) {
        if(MFPWarps.getInstance().getNameEditors().isEmpty()) return;

        UUID uuid = playerChatEvent.getPlayer().getUniqueId();

        if(!MFPWarps.getInstance().getNameEditors().containsKey(uuid)) return;

        playerChatEvent.setCancelled(true);

        Warp warp = MFPWarps.getInstance().getNameEditors().get(uuid);

        if(ChatColor.stripColor(C.color(playerChatEvent.getMessage())).equalsIgnoreCase("cancel")) {
            TL.CANCELLED_NAME_CHANGE.send(playerChatEvent.getPlayer());
            return;
        }

        if(ChatColor.stripColor(C.color(playerChatEvent.getMessage())).length() > CHAR_LENGTH) {
            TL.NAME_TOO_LONG.send(playerChatEvent.getPlayer(), new PlaceHolder("%length%", CHAR_LENGTH));
            return;
        }

        String name = ChatColor.stripColor(C.color(playerChatEvent.getMessage()));

        boolean found = false;
        for(Warp warps : WarpData.allWarps) {
            String allNames = ChatColor.stripColor(C.color(warps.getName()));
            if(name.equalsIgnoreCase(allNames)) {
                TL.NAME_TAKEN.send(playerChatEvent.getPlayer());
                found = true;
                break;
            }
        }

        if(found) {
            return;
        }

        warp.setName(playerChatEvent.getMessage());
        MFPWarps.getInstance().getNameEditors().remove(uuid);
        TL.SET_WARP_NAME.send(playerChatEvent.getPlayer(), new PlaceHolder("%name%", C.color(playerChatEvent.getMessage())));
    }
}
