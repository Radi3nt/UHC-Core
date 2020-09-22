package fr.radi3nt.loupgarouuhc.listeners;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.classes.stats.Stats;
import fr.radi3nt.loupgarouuhc.utilis.Updater;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void OnPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        Player p = e.getPlayer();

        LGPlayer lgp = LGPlayer.thePlayer(e.getPlayer());
        if (!players.contains(lgp)) {
            players.add(lgp);
        }


        e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getDisplayName() + " (" + ChatColor.YELLOW + players.size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + Bukkit.getMaxPlayers() + ChatColor.GRAY + ")");
        HoloStats.updateAll();
        if ((lgp.getGame() == null || lgp.isDead() ) || lgp.getGame().getGameTimer().getTicks() >= lgp.getGame().getParameters().getPvpActivate()) {
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
                if (player.getGame() != null) {
                    lgp.setChat(DeadChatI);
                }
            }
        }

        fr.radi3nt.loupgarouuhc.classes.config.Config config = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "/players", e.getPlayer().getName() + ".yml");

        Stats stats = new Stats();
                stats.setGameNumber(config.getConfiguration().getInt(e.getPlayer().getName() + ".games"));
                stats.setWinnedGames(config.getConfiguration().getInt(e.getPlayer().getName() + ".wins"));
                stats.setKills(config.getConfiguration().getInt(e.getPlayer().getName() + ".kills"));
                stats.setPoints(config.getConfiguration().getInt(e.getPlayer().getName() + ".points"));
                lgp.setStats(stats);


                /*
        Updater.UpdateResults results = LoupGarouUHC.getUpdater().checkForUpdates();
        if (results.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE) && p.hasPermission("lg.updater")) {
            p.sendMessage(prefix + ChatColor.GREEN + " Update found:" + results.getVersion() + ". Current is " + LoupGarouUHC.getVersion());
        }

                 */
    }
}
