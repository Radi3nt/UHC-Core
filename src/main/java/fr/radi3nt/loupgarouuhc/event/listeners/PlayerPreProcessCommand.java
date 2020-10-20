package fr.radi3nt.loupgarouuhc.event.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerPreProcessCommand implements Listener {

    @EventHandler
    public void PlayerPreProcessCommand (PlayerCommandPreprocessEvent e) {
        String[] strs = e.getMessage().split(" ");
        String label = strs[0].replace("/", "");
        String[] args = new String[strs.length - 1];
        String argsS = "";
        for (int i = 1; i < strs.length; i++) {
            args[i - 1] = strs[i];
            argsS += strs[i] + " ";
        }
        if (label.equalsIgnoreCase("msg") || label.equalsIgnoreCase("whisper") || label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("w")) {
            e.getPlayer().performCommand("lg msg " + argsS);
            e.setCancelled(true);
        }
    }

}
