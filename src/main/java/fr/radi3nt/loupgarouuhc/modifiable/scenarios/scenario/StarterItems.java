package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnStartGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StarterItems extends Scenario {

    private final List<ItemStack> items;

    public StarterItems(LGGame game, List<ItemStack> items) {
        super(game);
        this.items = items;
    }

    public static String getName() {
        return "StarterItems";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.COOKED_BEEF);
    }

    @Override
    public void register() {
        super.register();
    }

    @ScenarioEvent
    public void event(OnStartGame e) {
        if (game == e.getGame()) {
            if (isActive()) {
                for (LGPlayer gamePlayer : e.getGame().getGamePlayers()) {
                    for (ItemStack item : items) {
                        gamePlayer.getPlayer().getInventory().addItem(item.clone());
                    }
                }
            }
        }
    }

}
