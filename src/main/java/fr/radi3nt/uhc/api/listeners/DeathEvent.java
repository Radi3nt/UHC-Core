package fr.radi3nt.uhc.api.listeners;

import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.reasons.Reason;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.game.reasons.ReasonSlainByPlayer;
import fr.radi3nt.uhc.api.game.reasons.ReasonUndefined;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.math.BigDecimal;

public class DeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnDamageEvent(EntityDamageEvent e) {
        if (e.isCancelled())
            return;

        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.getHealth()-e.getFinalDamage()<=0) {
                UHCPlayer uhcPlayer = UHCPlayer.thePlayer(player);
                UHCGame game = uhcPlayer.getGameData().getGame();
                if (uhcPlayer.isPlaying()) {

                    e.setCancelled(true);

                    uhcPlayer.setChat(game.getData().getDeathChat());

                    /*
                    uhcPlayer.getPlayer().setCustomName("DEAD");
                    uhcPlayer.getPlayer().setCustomNameVisible(true);
                    uhcPlayer.getPlayer().chat("coucou");
                    
                     */

                    UHCPlayer lgDamager = null;
                    if (e instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) e;
                        if (damageByEntityEvent.getDamager() instanceof Player) {
                            lgDamager = UHCPlayer.thePlayer((Player) damageByEntityEvent.getDamager());
                        }
                        if (damageByEntityEvent.getDamager() instanceof Projectile) {
                            Projectile projectile = (Projectile) damageByEntityEvent.getDamager();
                            if (projectile.getShooter() instanceof Player) {
                                lgDamager = UHCPlayer.thePlayer((Player) projectile.getShooter());
                            }
                        }
                    }

                    if (lgDamager != null && (!lgDamager.isPlaying() || !lgDamager.getGameData().getGame().equals(game)))
                        lgDamager = null;

                    uhcPlayer.getPlayerStats().refresh();
                    player.setMaxHealth(20.0D);
                    player.setHealth(20.0D);
                    uhcPlayer.getPlayerStats().setGameMode(GameMode.SPECTATOR);
                    uhcPlayer.getPlayerStats().update();

                    Reason reason = new ReasonUndefined();
                    if (lgDamager != null)
                        reason = new ReasonSlainByPlayer(lgDamager);

                    Bukkit.getPluginManager().callEvent(new UHCPlayerKilledEvent(game, uhcPlayer, reason));
                }
            }
        }
    }

}