package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoubleJump extends Scenario {

    private final HashMap<UUID, Long> jumpTime = new HashMap<>();

    public DoubleJump(UHCGame game) {
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
        for (UHCPlayer gamePlayer : game.getAlivePlayers()) {
            if (gamePlayer.getPlayer() != null) {

                gamePlayer.getPlayer().setAllowFlight(false);
                gamePlayer.getPlayer().setFlying(false);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Player) {
            UHCPlayer lgp = UHCPlayer.thePlayer((Player) event.getEntity());
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

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if (game.getGameTimer().getTicks() == 0)
            return;

        UHCPlayer lgp = UHCPlayer.thePlayer(event.getPlayer());
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
                Bukkit.getScheduler().runTaskLater(UHCCore.getPlugin(), () -> {
                    player.setAllowFlight(false);
                }, 20L);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        UHCPlayer lgp = UHCPlayer.thePlayer(e.getPlayer());
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
