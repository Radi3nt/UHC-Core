package fr.radi3nt.uhc.api.listeners;

import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.Reason;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void OnDamageEvent(EntityDamageEvent e) {
        if (e.getDamage() != 0) {
            if (e.getEntity() instanceof Player) {
                UHCGame game = UHCPlayer.thePlayer((Player) e.getEntity()).getGameData().getGame();
                if (e.getEntity().isDead() || ((Player) e.getEntity()).getHealth() - e.getDamage() <= 0) {
                    Player player = (Player) e.getEntity();
                    UHCPlayer uhcPlayer = UHCPlayer.thePlayer((Player) e.getEntity());
                    if (uhcPlayer.isPlaying()) {
                        e.setCancelled(true);
                        uhcPlayer.sendTitle(ChatColor.RED + "You died", ChatColor.GRAY + "You can only spectate the game", 5, 5 * 20, 5);

                        uhcPlayer.setChat(game.getData().getDeathChat());
                        player.setMaxHealth(20.0D);
                        player.setHealth(20.0D);


                        uhcPlayer.getPlayerStats().refresh();

                        UHCPlayer lgDamager = null;
                        if (e instanceof EntityDamageByEntityEvent || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                            EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;
                            if (e1.getDamager() instanceof Player) {
                                   lgDamager = UHCPlayer.thePlayer((Player) e1.getDamager());

                            }
                            if (e1.getDamager() instanceof Projectile) {
                                Projectile projectile = (Projectile) e1.getDamager();
                                if (projectile.getShooter() instanceof Player) {
                                    lgDamager = UHCPlayer.thePlayer((Player) projectile.getShooter());
                                }
                            }
                        }
                        if (lgDamager!=null && (!lgDamager.isPlaying() || !lgDamager.getGameData().getGame().equals(game)))
                            lgDamager=null;

                        uhcPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);

                        Reason reason = Reason.KILLED_UNDEFINED;
                        if (lgDamager != null)
                            reason = Reason.KILLED_BY_PLAYER;
                        else if (e instanceof EntityDamageByEntityEvent)
                            reason = Reason.KILLED_BY_MOB;

                        Bukkit.getPluginManager().callEvent(new UHCPlayerKilledEvent(game, uhcPlayer, lgDamager, reason, player.getLocation()));
                    }
                }
            }
        }
    }

}
