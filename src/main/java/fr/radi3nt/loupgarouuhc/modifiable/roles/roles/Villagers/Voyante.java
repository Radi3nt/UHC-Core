package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleSort;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefix;
import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.prefixPrivé;

public class Voyante extends Role {


    private boolean canSee = false;

    public Voyante(LGGame game) {
        super(game);
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {
        this.canSee=false;
    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, true, false), true);
        lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.GOLD + " Tu peut voir le role d'un joueur en faisant /lg role see <player>");
        this.canSee=true;
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (!canSee) {
                    cancel();
                }
                if (i == 60 * 5 * 20) {
                    cancel();
                    canSee = false;
                }
                i++;
            }

        }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1, 0L);
    }

    @Override
    public void OnKillSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {

    }

    @Override
    public void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {

    }

    @Override
    public void OnDiscoverRole(LGGame game, LGPlayer lgp) {
        ItemStack bookshelves = new ItemStack(Material.BOOKSHELF, 4);
        ItemStack obsis = new ItemStack(Material.OBSIDIAN, 4);
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), bookshelves);
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), obsis);
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, true, false), true);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.VOYANTE;
    }

    public boolean canSee() {
        return canSee;
    }
    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }
}
