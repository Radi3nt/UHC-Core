package fr.radi3nt.uhc.uhc.listeners;

import fr.radi3nt.uhc.api.events.UHCPlayerKillAnotherEvent;
import fr.radi3nt.uhc.api.player.Attribute;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UHCPlayerKillAnotherListener implements Listener {

    @EventHandler
    public void OnKillEvent(UHCPlayerKillAnotherEvent e) {
        UHCPlayer lgp = e.getKiller();
        lgp.getGameInformation().putAttribute("kills",  new Attribute<>( ((Integer) lgp.getGameInformation().getAttributeOrDefault("kills", new Attribute<>(0)).getObject()) + 1));
    }

}