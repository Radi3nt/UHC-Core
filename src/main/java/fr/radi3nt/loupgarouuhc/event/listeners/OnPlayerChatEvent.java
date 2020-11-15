package fr.radi3nt.loupgarouuhc.event.listeners;

import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class OnPlayerChatEvent implements Listener {

    @EventHandler
    public void OnPlayerChatEvent(PlayerChatEvent e) {
        UHCPlayer lgp = UHCPlayer.thePlayer(e.getPlayer());
        lgp.getChat().sendMessage(lgp, e.getMessage());
        e.setCancelled(true);
    }

}
