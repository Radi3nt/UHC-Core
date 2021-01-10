package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.events.UHCPlayerKillAnotherEvent;
import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class NoClean extends Scenario {

    private final HashMap<UHCPlayer, Integer> players = new HashMap<>();
    private int time = 30;

    public NoClean(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("NoClean").setItemStack(new ItemStack(Material.WOOD_SWORD)).setDescription("Cancel player damage for a certain amount of tme after killing someone");
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        if (gameTimer.getGame() == game) {
            if (isActive()) {
                for (Map.Entry<UHCPlayer, Integer> lgPlayerIntegerEntry : players.entrySet()) {
                    lgPlayerIntegerEntry.setValue(lgPlayerIntegerEntry.getValue() + 1);
                    if (lgPlayerIntegerEntry.getValue() >= time * 20) {
                        players.remove(lgPlayerIntegerEntry.getKey());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void event(UHCPlayerKillAnotherEvent e) {
        if (e.getGame() == game) {
            if (isActive()) {
                players.put(e.getKiller(), 0);
            }
        }
    }

    @EventHandler
    public void event(EntityDamageEvent e) {
        if (isActive()) {
            if (e.getEntity() instanceof Player) {
                UHCPlayer lgp = UHCPlayer.thePlayer((Player) e.getEntity());
                if (lgp.getGameData().getGame() == game) {
                    if (e instanceof EntityDamageByEntityEvent || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                        EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;
                        if (e1.getDamager() instanceof Player) {
                            UHCPlayer damager = UHCPlayer.thePlayer((Player) e1.getDamager());
                            if (players.containsKey(lgp)) {
                                if (e1.getDamager() instanceof Player) {
                                    //todo message
                                    e.setCancelled(true);
                                }
                                if (e1.getDamager() instanceof Projectile) {
                                    Projectile projectile = (Projectile) e1.getDamager();
                                    if (projectile.getShooter() instanceof Player) {
                                        //todo message
                                        e.setCancelled(true);
                                    }
                                }
                            } else {
                                if (e1.getEntity() instanceof Player) {
                                    if (players.remove(damager) != null) {
                                        //todo message
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @ScenarioGetter(name = "Time")
    public Integer getTime() {
        return time;
    }

    @ScenarioSetter(name = "Time")
    public void setTime(Integer time) {
        this.time = time;
    }
}
