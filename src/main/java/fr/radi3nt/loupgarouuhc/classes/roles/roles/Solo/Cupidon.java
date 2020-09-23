package fr.radi3nt.loupgarouuhc.classes.roles.roles.Solo;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.SecureRandom;
import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefix;
import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefixPrivé;

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
                    canCouple = false;
                    if (game.getGamePlayers().size() >= 2) {
                        ArrayList<LGPlayer> players1 = (ArrayList<LGPlayer>) game.getGamePlayers().clone();
                        LGPlayer lgp1 = null;
                        LGPlayer lgp2 = null;

                        int i = 0;
                        while ((lgp1 == null || lgp1.getCouple() != null) && i <= 1000) {
                            lgp1 = players1.get(new SecureRandom().nextInt(players1.size()));
                            i++;
                        }
                        if (lgp1 == null) {
                            return;
                        }

                        players1.remove(lgp1);
                        i = 0;
                        while ((lgp2 == null || lgp2.getCouple() != null) && i <= 1000) {
                            lgp2 = players1.get(new SecureRandom().nextInt(players1.size()));
                            i++;
                        }
                        if (lgp2 == null) {
                            return;
                        }

                        lgp1.setCouple(lgp2);
                        lgp2.setCouple(lgp1);


                        lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " Tu n'a pas su te decider, ton instinct l'a fait pour toi: tu a uni " + ChatColor.DARK_AQUA + lgp1.getName() + ChatColor.BLUE + " et " + ChatColor.DARK_AQUA + lgp2.getName());
                        lgp1.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " Tu es maintenant uni avec " + ChatColor.DARK_AQUA + lgp2.getName() + ChatColor.BLUE + ".\nSi l'un de vous meurt, l'autre ne pourras supporter cette souffrance et se suicidera immédiatement.");
                        lgp2.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " Tu es maintenant uni avec " + ChatColor.DARK_AQUA + lgp1.getName() + ChatColor.BLUE + ".\nSi l'un de vous meurt, l'autre ne pourras supporter cette souffrance et se suicidera immédiatement.");


                    }
                }
                if (canCouple = false) {
                    cancel();
                }
                i++;
            }

        }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1, 0L);
    }
}
