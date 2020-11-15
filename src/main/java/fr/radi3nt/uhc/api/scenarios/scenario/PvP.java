package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.chats.Chat;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PvP extends Scenario {

    protected final List<Integer> timeAlert;
    private final int time;
    private boolean timerActivated = true;
    private boolean pvp = false;

    public PvP(UHCGame game, int time, List<Integer> timeAlert) {
        super(game);
        this.time = time;
        this.timeAlert = timeAlert;
    }

    public static String getName() {
        return "PvP";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_SWORD);
    }

    @Override
    public void register() {
        super.register();
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        if (gameTimer.getGame().equals(game)) {
            if (isActive()) {
                if (timerActivated) {
                    for (Integer integer : timeAlert) {
                        if (tick == integer) {
                            Chat.broadcastIdMessage(getMessagesId() + "soon", game.getDeadAndAlivePlayers().toArray(new UHCPlayer[0]));
                        }
                    }
                    if (tick == time) {
                        activatePvp();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamagingOtherPlayer(EntityDamageByEntityEvent e) {
        if (isActive()) {
            if (e.getEntity() instanceof Player) {
                if (e.getDamager() instanceof Player) {
                    UHCPlayer player = UHCPlayer.thePlayer((Player) e.getDamager());
                    if (player.isInGame() && player.getGameData().getGame().equals(game))
                        if (!game.getPvP().isPvp()) {
                            e.setCancelled(true);
                        }
                }
                if (e.getDamager() instanceof Projectile) {
                    Projectile projectile = (Projectile) e.getDamager();
                    if (projectile.getShooter() instanceof Player) {
                        UHCPlayer player = UHCPlayer.thePlayer((Player) projectile.getShooter());
                        if (player.isInGame() && player.getGameData().getGame().equals(game))
                            if (!game.getPvP().isPvp()) {
                                e.setCancelled(true);
                            }
                    }
                }
            }
        }
    }

    public void activatePvp() {
        pvp = true;
        Chat.broadcastIdMessage(getMessagesId() + "on", game.getDeadAndAlivePlayers().toArray(new UHCPlayer[0]));
    }

    public void deactivatePvp() {
        pvp = false;
        Chat.broadcastIdMessage(getMessagesId() + "off", game.getDeadAndAlivePlayers().toArray(new UHCPlayer[0]));
    }

    @ScenarioCommand
    public void onCommand(CommandUtilis command) {
        if (isActive()) {
            String[] activate = new String[3];
            String base = "uhc.pvp.";
            activate[0] = base + "activate";
            activate[1] = base + "active";
            activate[2] = base + "on";
            String[] deactivate = new String[2];
            deactivate[0] = base + "deactivate";
            deactivate[1] = base + "off";
            try {
                if (command.executeCommand(activate, "uhc.pvp.activate", 0, CommandUtilis.Checks.GAME)) {
                    activatePvp();
                } else if (command.executeCommand(deactivate, "uhc.pvp.deactivate", 0, CommandUtilis.Checks.GAME)) {
                    deactivatePvp();
                } else if (command.executeCommand(base + "timer.off", "uhc.pvp.timer", 0, CommandUtilis.Checks.GAME)) {
                    timerActivated = false;
                } else if (command.executeCommand(base + "timer.on", "uhc.pvp.timer", 0, CommandUtilis.Checks.GAME)) {
                    timerActivated = true;
                }
            } catch (NoPermissionException e) {

            } catch (NoArgsException e) {

            }
        }
    }

    public int getTime() {
        return time;
    }

    public List<Integer> getTimeAlert() {
        return timeAlert;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isTimerActivated() {
        return timerActivated;
    }

    public void setTimerActivated(boolean timerActivated) {
        this.timerActivated = timerActivated;
    }
}
