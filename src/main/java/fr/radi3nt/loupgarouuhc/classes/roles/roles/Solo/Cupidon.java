package fr.radi3nt.loupgarouuhc.classes.roles.roles.Solo;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class Cupidon extends Role {

    public boolean canCouple = false;

    public Cupidon(LGGame game) {
        super(game);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.CUPIDON;
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
    }

    @Override
    public void OnKillSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {

    }

    @Override
    public void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {

    }

    @Override
    public void OnDiscoverRole(LGGame game, LGPlayer lgp) {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);

        ItemStack arrows = new ItemStack(Material.ARROW, 64);

        ItemStack string = new ItemStack(Material.STRING, 3);

        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), bow);
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), arrows);
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), string);
        canCouple=true;
        lgp.sendMessage(prefix + ChatColor.GOLD + " Tu peut choisir un couple en faisant /lg role couple <pseudo> <pseudo>");
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i==60*5*20) {
                    cancel();
                    canCouple=false;
                    //todo auto role
                }
                i++;
            }

        }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1, 0L);
    }
}
