package fr.radi3nt.loupgarouuhc.event.customlisteners;

import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.event.events.WinConditionsCheckEvent;
import fr.radi3nt.loupgarouuhc.roles.WinType;
import fr.radi3nt.loupgarouuhc.roles.roles.Solo.Cupidon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OnWinCheck implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWinCheck(WinConditionsCheckEvent e) {
        if (!e.isCancelled()) {
            if (e.getGame().getRoles().size() <= 1) {
                if (e.getGame().getRoles().isEmpty()) {
                    e.setVictoryTeam(WinType.NONE);
                } else {
                    e.setVictoryTeam(e.getGame().getRoles().get(0).getWinType());
                }
                e.setCancelled(true);
            } else {
                WinType wintype = e.getGame().getRoles().get(0).getWinType();
                if (wintype!=WinType.SOLO) {
                    boolean valid = true;
                    for (UHCPlayer player : e.getGame().getGamePlayers()) {
                        if (player.getGameData().getRole().isInfected() && wintype == WinType.LOUP_GAROU)
                            continue;
                        if (player.getGameData().getRole().getWinType() != wintype) {
                            valid=false;
                            break;
                        }
                        if (player.getGameData().getRole().getWinType() == WinType.SOLO) {
                            valid=false;
                            break;
                        }
                    }
                    if (valid) {
                        e.setVictoryTeam(wintype);
                        e.setCancelled(true);
                        return;
                    }
                }
                //couple
                if (e.getGame().getRoles().size()<=3) {
                    int size = 0;
                    for (UHCPlayer lgp : e.getGame().getGamePlayers()) {
                        if (lgp.getGameData().isInCouple())
                            size++;
                        else if (lgp.getGameData().getRoleIdentity().equals(Cupidon.getStaticRoleIdentity()))
                            continue;
                        else
                            return;
                    }
                    if (size==2) {
                        e.setVictoryTeam(WinType.COUPLE);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
