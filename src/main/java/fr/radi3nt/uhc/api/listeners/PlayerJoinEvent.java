package fr.radi3nt.uhc.api.listeners;

import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.instances.DefaultsParameters;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.stats.HoloStats;
import fr.radi3nt.uhc.api.stats.Hologram;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void OnPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UHCPlayer lgp = UHCPlayer.thePlayer(e.getPlayer());
        UHCCore.getPlayers().add(lgp);

        lgp.getPlayerStats().update();
        lgp.loadSavedLang();
        lgp.loadStats();

        //PACKETS THINGS
        for (HoloStats holoStats : HoloStats.getCachedHolo()) {
            for (Hologram hologram : holoStats.getHologramsStand()) {
                hologram.display(e.getPlayer());
            }
        }

        e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getDisplayName() + " (" + ChatColor.YELLOW + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + Bukkit.getMaxPlayers() + ChatColor.GRAY + ")");
        HoloStats.updateAll();
        if (!lgp.isInGame() || (lgp.isInGame() && lgp.getGameData().isDead()) || ((lgp.isInGame() && lgp.getGameData().getGame().getPvP().isPvp() && !lgp.getGameData().getGame().getParameters().getDisconnectParameters().canReconnectInPvp()))) {
            p.setWalkSpeed(0.2F);
            p.resetMaxHealth();
            p.setFoodLevel(20);
            p.setHealth(20);
            p.setExp(0);
            p.setLevel(0);
            for (PotionEffectType ption : PotionEffectType.values()) {
                if (ption != null)
                    p.removePotionEffect(ption);
            }
            lgp.getPlayer().getInventory().clear();

            e.getPlayer().teleport(new DefaultsParameters().getSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            e.getPlayer().setGameMode(Bukkit.getServer().getDefaultGameMode());
            lgp.setChat(UHCCore.DEFAULT_CHAT);
            lgp.getPlayerStats().refresh();
        } else {
            if (lgp.getGameData().getGame().getState() == GameState.PLAYING) {
                p.setWalkSpeed(0.2f);
            }
        }


                /*
        Updater.UpdateResults results = LoupGarouUHC.getUpdater().checkForUpdates();
        if (results.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE) && p.hasPermission("lg.updater")) {
            p.sendMessage(prefix + ChatColor.GREEN + " Update found:" + results.getVersion() + ". Current is " + LoupGarouUHC.getVersion());
        }

                 */
        /*
        p.getInventory().addItem(new GameInfoGUI().createBook(lgp.getLanguage(), RoleType.VILLAGER));
        p.getInventory().addItem(new GameInfoGUI().createBook(lgp.getLanguage(), RoleType.NEUTRAL));
        p.getInventory().addItem(new GameInfoGUI().createBook(lgp.getLanguage(), RoleType.LOUP_GAROU));

         *///todo livres GUI
    }
}
