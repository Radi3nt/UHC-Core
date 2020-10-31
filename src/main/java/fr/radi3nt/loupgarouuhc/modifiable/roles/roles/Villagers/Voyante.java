package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.VillagerRoleIdentity;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefix;
import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefixPrivé;

public class Voyante extends Role {


    private boolean canSee = false;

    public Voyante(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        ArrayList<ItemStack> rolesItems = new ArrayList<>();
        rolesItems.add(new ItemStack(Material.BOOKSHELF, 4));
        rolesItems.add(new ItemStack(Material.OBSIDIAN, 4));

        ArrayList<PotionEffect> potionEffects = new ArrayList<>();
        potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999 * 20, 0, true, false, Color.AQUA));

        return new VillagerRoleIdentity("Voyante", rolesItems, potionEffects, 20).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    public void night(LGGame game, LGPlayer lgp) {
        this.canSee = false;
    }

    @Override
    public void day(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void newEpisode(LGGame game, LGPlayer lgp) {
        lgp.sendMessage(getPrefix() + " " + getPrefixPrivé() + ChatColor.GOLD + " Tu peut voir le role d'un joueur en faisant /lg role see <player>");
        this.canSee = true;
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
    public void killSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {

    }

    @Override
    public void killed(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {

    }

    @Override
    public void discoverRole(LGGame game, LGPlayer lgp) {

    }

    public boolean canSee() {
        return canSee;
    }
    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }
}
