package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioCommand;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefix;

public class OreLimiter extends Scenario {

    protected Map<Material, Integer> oreLimit = new HashMap<>();
    private final Map<LGPlayer, Map<Material, Integer>> playersOre = new HashMap<>();

    public OreLimiter(LGGame game) {
        super(game);
        oreLimit.put(Material.DIAMOND_ORE, 17);
    }

    public static String getName() {
        return "OreLimiter";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_ORE);
    }


    @ScenarioEvent
    public void event(BlockBreakEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                if (oreLimit.containsKey(e.getBlock().getType())) {
                    if (playersOre.getOrDefault(player, new HashMap<>()).getOrDefault(e.getBlock().getType(), 0) < oreLimit.getOrDefault(e.getBlock().getType(), 0) + 1) {
                        Map<Material, Integer> map = playersOre.getOrDefault(player, new HashMap<>());
                        map.put(e.getBlock().getType(), map.getOrDefault(e.getBlock().getType(), 0) + 1);
                        playersOre.put(player, map);
                    } else {
                        player.sendMessage(prefix + " " + ChatColor.RED + "Vous avez dépassé la limite de " + e.getBlock().getType().toString());
                        e.getBlock().setType(Material.AIR);
                        e.setCancelled(true);
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, SoundCategory.AMBIENT, 1, 0.3f);
                    }
                }
            }
        }
    }

    @ScenarioCommand
    public void onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (isActive()) {
            fr.radi3nt.loupgarouuhc.commands.Command command1 = new fr.radi3nt.loupgarouuhc.commands.Command(commandSender, command, s, strings);
            if (command1.executeCommand("uhc.limiter", "uhc.limiter.change", 3, fr.radi3nt.loupgarouuhc.commands.Command.Checks.GAME)) {
                Player target = Bukkit.getPlayerExact(strings[1]);
                Material material = Material.matchMaterial(strings[2]);
                Integer integer = null;
                try {
                    integer = Integer.parseInt(strings[3]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (target != null && integer != null && material != null) {
                    LGPlayer tlgp = LGPlayer.thePlayer(target);
                    Map<Material, Integer> map = playersOre.getOrDefault(tlgp, new HashMap<>());
                    map.put(material, integer);
                    playersOre.put(tlgp, map);
                } else {
                    //todo error message
                }
            }
        }
    }

    @ScenarioGetter(name = "Ore Limit")
    public Map<Material, Integer> getOreLimit() {
        return oreLimit;
    }

    @ScenarioSetter(name = "Ore Limit")
    public void setOreLimit(Map<Material, Integer> oreLimit) {
        this.oreLimit = oreLimit;
    }
}
