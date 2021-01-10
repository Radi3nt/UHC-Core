package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioCommand;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import fr.radi3nt.uhc.uhc.UHCCore;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OreLimiter extends Scenario {

    protected Map<Material, Integer> oreLimit = new HashMap<>();
    private final Map<UHCPlayer, Map<Material, Integer>> playersOre = new HashMap<>();

    public OreLimiter(UHCGame game) {
        super(game);
        oreLimit.put(Material.DIAMOND_ORE, 17);
    }

    public static ScenarioData getData() {
        return new ScenarioData("OreLimiter").setItemStack(new ItemStack(Material.DIAMOND_ORE)).setDescription("Limit the amount of ores a player can mine in a game");
    }


    @EventHandler
    public void event(BlockBreakEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                if (oreLimit.containsKey(e.getBlock().getType())) {
                    if (playersOre.getOrDefault(player, new HashMap<>()).getOrDefault(e.getBlock().getType(), 0) < oreLimit.getOrDefault(e.getBlock().getType(), 0)) {
                        Map<Material, Integer> map = playersOre.getOrDefault(player, new HashMap<>());
                        map.put(e.getBlock().getType(), map.getOrDefault(e.getBlock().getType(), 0) + 1);
                        playersOre.put(player, map);
                    } else {
                        TextComponent giveMessage = new TextComponent();
                        TextComponent item = new TextComponent(CraftItemStack.asNMSCopy(new ItemStack(e.getBlock().getType())).getName().toLowerCase());
                        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(CraftItemStack.asNMSCopy(new ItemStack(e.getBlock().getType())).save(new NBTTagCompound()).toString())});
                        item.setHoverEvent(he);
                        item.setColor(net.md_5.bungee.api.ChatColor.DARK_RED);
                        TextComponent username = new TextComponent(UHCCore.getPrefix() + " " + ChatColor.RED + "Vous avez dépassé la limite de ");
                        username.setColor(net.md_5.bungee.api.ChatColor.RED);
                        giveMessage.addExtra(username);
                        giveMessage.addExtra(item);
                        player.getPlayer().spigot().sendMessage(giveMessage);

                        //player.sendMessage(getPrefix() + " " + ChatColor.RED + "Vous avez dépassé la limite de " + e.getBlock().getType().toString());
                        e.getBlock().setType(Material.AIR);
                        e.setCancelled(true);
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, SoundCategory.AMBIENT, 1, 0.3f);
                    }
                }
            }
        }
    }

    @ScenarioCommand
    public void onCommand(CommandUtilis command) {
        if (isActive()) {
            try {
                if (command.executeCommand("uhc.limiter", "uhc.limiter.change", 3, CommandUtilis.Checks.GAME)) {
                    Player target = Bukkit.getPlayerExact(command.getArgs()[1]);
                    Material material = Material.matchMaterial(command.getArgs()[2]);
                    Integer integer = null;
                    try {
                        integer = Integer.parseInt(command.getArgs()[3]);
                    } catch (NumberFormatException e) {
                        Logger.getGeneralLogger().log(e);
                    }

                    if (target != null && integer != null && material != null) {
                        UHCPlayer tlgp = UHCPlayer.thePlayer(target);
                        Map<Material, Integer> map = playersOre.getOrDefault(tlgp, new HashMap<>());
                        map.put(material, integer);
                        playersOre.put(tlgp, map);
                    } else {
                        //todo error message
                    }
                }
            } catch (NoPermissionException | NoArgsException e) {

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
