package fr.radi3nt.loupgarouuhc.event.listeners;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.classes.stats.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void OnPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setWalkSpeed(0.2f);
        LGPlayer lgp = LGPlayer.thePlayer(e.getPlayer());
        if (!LoupGarouUHC.getPlayers().contains(lgp)) {
            LoupGarouUHC.getPlayers().add(lgp);
        }

        lgp.getPlayerStats().update();
        lgp.loadSavedLang();
        lgp.loadStats();

        //PACKETS THINGS
        for (HoloStats holoStats : HoloStats.getCachedHolo()) {
            for (Hologram hologram : holoStats.getHologramsStand()) {
                hologram.display(e.getPlayer());
            }
        }


        e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getDisplayName() + " (" + ChatColor.YELLOW + LoupGarouUHC.getPlayers().size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + Bukkit.getMaxPlayers() + ChatColor.GRAY + ")");
        HoloStats.updateAll();
        lgp.getPlayer().setWalkSpeed(0.2F);
        if (!lgp.isInGame() || (lgp.isInGame() && lgp.getGameData().isDead()) || ((lgp.isInGame() && lgp.getGameData().getGame().getPvP().isPvp() && !lgp.getGameData().getGame().getParameters().isCanReconnectInPvp()))) {
            p.resetMaxHealth();
            p.setFoodLevel(20);
            p.setHealth(20);
            p.setExp(0);
            p.setLevel(0);
            for (PotionEffectType ption : PotionEffectType.values()) {
                try {
                    p.removePotionEffect(ption);
                } catch (Exception e1) {

                }
            }
            lgp.getPlayer().getInventory().clear();

            e.getPlayer().teleport(parameters.getSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            e.getPlayer().setGameMode(Bukkit.getServer().getDefaultGameMode());
            lgp.setChat(GeneralChatI);
            for (LGPlayer player : LoupGarouUHC.getPlayers()) {
                if (player.isInGame()) {
                    lgp.setChat(DeadChatI);
                }
            }
            lgp.getPlayerStats().refresh();
        }


                /*
        Updater.UpdateResults results = LoupGarouUHC.getUpdater().checkForUpdates();
        if (results.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE) && p.hasPermission("lg.updater")) {
            p.sendMessage(prefix + ChatColor.GREEN + " Update found:" + results.getVersion() + ". Current is " + LoupGarouUHC.getVersion());
        }

                 */
    }
}
