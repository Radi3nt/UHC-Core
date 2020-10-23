package fr.radi3nt.loupgarouuhc.event.listeners;

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
        if (!players.contains(lgp)) {
            players.add(lgp);
        }

        lgp.loadSavedLang();
        lgp.loadStats();

        //PACKETS THINGS
        for (HoloStats holoStats : HoloStats.getCachedHolo()) {
            for (Hologram hologram : holoStats.getHologramsStand()) {
                hologram.display(e.getPlayer());
            }
        }


        e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getDisplayName() + " (" + ChatColor.YELLOW + players.size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + Bukkit.getMaxPlayers() + ChatColor.GRAY + ")");
        HoloStats.updateAll();
        if (!lgp.isInGame() || (lgp.isInGame() && lgp.getGameData().isDead()) || ((lgp.isInGame() && lgp.getGameData().getGame().getPvP().isPvp()))) {
            p.resetMaxHealth();
            p.setFoodLevel(20);
            p.setHealth(20);
            p.setWalkSpeed(0);
            p.setExp(0);
            p.setLevel(0);
            for (PotionEffectType ption : PotionEffectType.values()) {
                try {
                    p.removePotionEffect(ption);
                } catch (Exception e1) {

                }
            }
            lgp.getPlayer().setWalkSpeed(0.2F);
            lgp.getPlayer().getInventory().clear();

            e.getPlayer().teleport(parameters.getSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            e.getPlayer().setGameMode(Bukkit.getServer().getDefaultGameMode());
            lgp.setChat(GeneralChatI);
            for (LGPlayer player : players) {
                if (player.isInGame()) {
                    lgp.setChat(DeadChatI);
                }
            }
        }


                /*
        Updater.UpdateResults results = LoupGarouUHC.getUpdater().checkForUpdates();
        if (results.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE) && p.hasPermission("lg.updater")) {
            p.sendMessage(prefix + ChatColor.GREEN + " Update found:" + results.getVersion() + ". Current is " + LoupGarouUHC.getVersion());
        }

                 */
    }
}