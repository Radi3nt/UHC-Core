package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class XPBoost extends Scenario {

    protected int xpBoostPercentage = 25;
    protected Map<Material, Integer> xpMaterial = new HashMap<>();

    public XPBoost(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() {
        return new ScenarioData("XPBoost").setItemStack(new ItemStack(Material.BEACON)).setDescription("Add extra XP when receiving xp");
    }


    @EventHandler
    public void event(BlockBreakEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive() && !e.isCancelled()) {
                if (xpMaterial.containsKey(e.getBlock().getType())) {
                    (e.getBlock().getLocation().getWorld().spawn(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), ExperienceOrb.class)).setExperience(xpMaterial.get(e.getBlock().getType()));
                }
            }
        }
    }

    @EventHandler
    public void event(PlayerExpChangeEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                if (e.getAmount() > 0) {
                    e.getPlayer().giveExp((int) ((((float) xpBoostPercentage / 100) * 17) + (float) e.getAmount()));
                    e.setAmount(0);
                }
            }
        }
    }

    @ScenarioGetter(name = "XP Boost")
    public int getXpBoostPercentage() {
        return xpBoostPercentage;
    }

    @ScenarioSetter(name = "XP Boost")
    public void setXpBoostPercentage(int xpBoostPercentage) {
        this.xpBoostPercentage = xpBoostPercentage;
    }

    @ScenarioGetter(name = "XP Materials")
    public Map<Material, Integer> getXpMaterial() {
        return xpMaterial;
    }

    @ScenarioSetter(name = "XP Materials")
    public void setXpMaterial(Map<Material, Integer> xpMaterial) {
        this.xpMaterial = xpMaterial;
    }
}
