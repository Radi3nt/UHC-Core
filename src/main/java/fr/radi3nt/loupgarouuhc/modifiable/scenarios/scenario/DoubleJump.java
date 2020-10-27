package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoubleJump extends Scenario {

    private final HashMap<UUID, Long> jumpTime = new HashMap<>();

    public DoubleJump(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "DoubleJump";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.LEATHER_BOOTS);
    }

    @Override
    public void unregister() {
        super.unregister();
        for (Map.Entry<UUID, Long> uuidLongEntry : jumpTime.entrySet()) {
            Player player = Bukkit.getPlayer(uuidLongEntry.getKey());
            if (player != null) {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }

    @ScenarioEvent
    public void onDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Player) {
            LGPlayer lgp = LGPlayer.thePlayer((Player) event.getEntity());
            if (lgp.getGameData().getGame() == game) {
                if (isActive()) {
                    Player player = (Player) entity;
                    UUID uuid = player.getUniqueId();
                    if (this.jumpTime.containsKey(uuid)) {
                        long secs = (System.currentTimeMillis() - this.jumpTime.get(uuid)) / 1000L;
                        if (secs > 5L) {
                            this.jumpTime.remove(uuid);
                        } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                            event.setCancelled(true);
                            this.jumpTime.remove(uuid);
                        }
                    }
                }
            }
        }
    }

    @ScenarioEvent
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        LGPlayer lgp = LGPlayer.thePlayer(event.getPlayer());
        if (lgp.getGameData().getGame() == game) {
            if (isActive()) {

                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();

                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }

                event.setCancelled(true);
                player.setAllowFlight(false);
                player.setFlying(false);

                double vel = 0.4;
                player.setVelocity(player.getLocation().getDirection().multiply(vel).setY(0.8));

                this.jumpTime.put(uuid, System.currentTimeMillis());
                Bukkit.getScheduler().runTaskLater(LoupGarouUHC.getPlugin(), () -> {
                    player.setAllowFlight(false);
                }, 20L);
            }
        }
    }

    @ScenarioEvent
    public void onMove(PlayerMoveEvent e) {
        LGPlayer lgp = LGPlayer.thePlayer(e.getPlayer());
        if (lgp.getGameData().getGame() == game) {
            if (isActive()) {
                Player player = e.getPlayer();
                if (player.getGameMode() != GameMode.CREATIVE && player.isOnGround()) {
                    player.setAllowFlight(true);
                }
            }
        }
    }

}
