package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.chats.Chat;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class StartInvincibility extends Scenario {

    private int timeOfInvincibilityEnd = 30;

    public StartInvincibility(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("StartInvincibility").setItemStack(new ItemStack(Material.LAPIS_BLOCK)).setDescription("Cancel damage before a certain amount of time after the game started");
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        super.tick(gameTimer, tick);
        if (gameTimer.getGame().equals(game)) {
            if (isActive()) {
                if (tick == timeOfInvincibilityEnd * 20) {
                    Chat.broadcastIdMessage(getMessagesId() + "stop", game.getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamagingOtherPlayer(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            UHCPlayer player = UHCPlayer.thePlayer((Player) e.getEntity());
            if (player.isPlaying() && player.getGameData().getGame().equals(game)) {
                if (isActive()) {
                    if (player.getGameData().getGame().getGameTimer().getTicks() <= timeOfInvincibilityEnd * 20) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @ScenarioGetter(name = "Invincibility time")
    public int getTimeOfInvincibilityEnd() {
        return timeOfInvincibilityEnd;
    }

    @ScenarioSetter(name = "Invincibility time")
    public void setTimeOfInvincibilityEnd(int timeOfInvincibilityEnd) {
        this.timeOfInvincibilityEnd = timeOfInvincibilityEnd;
    }
}
