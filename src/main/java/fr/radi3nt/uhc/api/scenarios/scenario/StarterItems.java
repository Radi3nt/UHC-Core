package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.events.UHCGameStartsEvent;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StarterItems extends Scenario {

    protected List<ItemStack> items = new ArrayList<>();

    public StarterItems(UHCGame game) {
        super(game);
        items.add(new ItemStack(Material.COOKED_BEEF, 32));
    }

    public static String getName() {
        return "StarterItems";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.COOKED_BEEF);
    }

    @EventHandler
    public void event(UHCGameStartsEvent e) {
        if (game == e.getGame()) {
            if (isActive()) {
                for (UHCPlayer gamePlayer : e.getGame().getAlivePlayers()) {
                    for (ItemStack item : items) {
                        gamePlayer.getPlayer().getInventory().addItem(item.clone());
                    }
                }
            }
        }
    }

    @ScenarioGetter(name = "Item list")
    public List<ItemStack> getItems() {
        return items;
    }

    @ScenarioSetter(name = "Item list")
    public void setItems(List<ItemStack> items) {
        this.items = items;
    }
}
