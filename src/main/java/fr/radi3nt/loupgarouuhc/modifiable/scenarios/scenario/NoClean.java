package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnKilled;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import fr.radi3nt.loupgarouuhc.timer.GameTimer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class NoClean extends Scenario {

    private final HashMap<LGPlayer, Integer> players = new HashMap<>();
    private Integer time;

    public NoClean(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "NoClean";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.BED);
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        if (gameTimer.getGame() == game) {
            if (isActive()) {
                for (Map.Entry<LGPlayer, Integer> lgPlayerIntegerEntry : players.entrySet()) {
                    lgPlayerIntegerEntry.setValue(lgPlayerIntegerEntry.getValue() + 1);
                    if (lgPlayerIntegerEntry.getValue() >= time) {
                        players.remove(lgPlayerIntegerEntry.getKey());
                    }
                }
            }
        }
    }

    @ScenarioEvent
    public void event(OnKilled e) {
        if (e.getGame() == game) {
            if (isActive()) {
                players.put(e.getKiller(), 0);
            }
        }
    }

    @ScenarioEvent
    public void event(EntityDamageEvent e) {
        if (isActive()) {
            if (e.getEntity() instanceof Player) {
                LGPlayer lgp = LGPlayer.thePlayer((Player) e.getEntity());
                if (lgp.getGameData().getGame() == game) {
                    if (e instanceof EntityDamageByEntityEvent || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                        EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e;
                        LGPlayer damager = LGPlayer.thePlayer((Player) e1.getDamager());
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

    @ScenarioGetter(name = "Time")
    public Integer getTime() {
        return time;
    }

    @ScenarioSetter(name = "Time")
    public void setTime(Integer time) {
        this.time = time;
    }
}
