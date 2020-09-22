package fr.radi3nt.loupgarouuhc.listeners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class OnPlayerChatEvent implements Listener {

    @EventHandler
    public void OnPlayerChatEvent(PlayerChatEvent e) {
        LGPlayer lgp = LGPlayer.thePlayer(e.getPlayer());
        lgp.getChat().sendMessage(lgp, e.getMessage());
        e.setCancelled(true);
    }

}
