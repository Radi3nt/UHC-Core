package fr.radi3nt.loupgarouuhc.listeners;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;


public class OnBreakBlockEvent implements Listener {

    @EventHandler
    public void OnBreakBlockEvent(BlockBreakEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
        if (e.getBlock().getType()==Material.DIAMOND_ORE) {
            if (player.getGame() != null && !player.isDead()) {
                if (player.getDiamondMined() >= player.getGame().getParameters().getDiamondLimit()) {
                    e.getBlock().setType(Material.AIR);
                    player.sendMessage(prefix + " " + ChatColor.RED + "Vous avez dépassé la limite de diamands");
                } else {
                    player.setDiamondMined(player.getDiamondMined() + 1);
                }
            }
        }
            if (e.getBlock().getType() == Material.getMaterial("LEAVES")) {
                if (player.getGame() != null && !player.isDead()) {
                    SecureRandom random = new SecureRandom();
                    if (random.nextInt(100) < player.getGame().getParameters().getPercentageOfAppleDrop()) {
                        for (ItemStack item : e.getBlock().getDrops()) {
                            if (item.getType() == Material.APPLE) {
                                item.setType(Material.AIR);
                            }
                        }
                        player.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
                    }
                }
            }

        if (e.getBlock().getType() == Material.GRAVEL) {
            if (player.getGame() != null && !player.isDead()) {
                SecureRandom random = new SecureRandom();
                if (random.nextInt(100) < player.getGame().getParameters().getPercentageOfGravelDrop()) {
                    for (ItemStack item : e.getBlock().getDrops()) {
                        item.setType(Material.AIR);
                    }
                    player.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.FLINT));
                }
            }
        }

        if (player.getGame()!=null && player.getGame().getParameters().isCutClean()) {
            if (e.getBlock().getType() == Material.IRON_ORE) {
                e.setDropItems(false);
                player.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
            }
            if (e.getBlock().getType() == Material.GOLD_ORE) {
                player.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
            }
        }
    }
}
