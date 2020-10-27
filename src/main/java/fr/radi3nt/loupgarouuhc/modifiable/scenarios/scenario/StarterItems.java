package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnStartGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StarterItems extends Scenario {

    protected List<ItemStack> items = new ArrayList<>();

    public StarterItems(LGGame game) {
        super(game);
        items.add(new ItemStack(Material.COOKED_BEEF, 32));


        items.add(new ItemStack(Material.COAL, 64));
        items.add(new ItemStack(Material.IRON_ORE, 64));
        items.add(new ItemStack(Material.FURNACE, 64));
        items.add(new ItemStack(Material.IRON_PICKAXE, 64));
    }

    public static String getName() {
        return "StarterItems";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.COOKED_BEEF);
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

    @ScenarioGetter(name = "Item list")
    public List<ItemStack> getItems() {
        return items;
    }

    @ScenarioSetter(name = "Item list")
    public void setItems(List<ItemStack> items) {
        this.items = items;
    }
}
