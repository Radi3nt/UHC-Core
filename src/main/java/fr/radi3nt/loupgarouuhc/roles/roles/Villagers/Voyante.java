package fr.radi3nt.loupgarouuhc.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.VillagerRoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.attributes.Power;
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

public class Voyante extends Role implements Power {


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
    public void night(LGGame game, UHCPlayer lgp) {
        this.canSee = false;
    }

    @Override
    public void day(LGGame game, UHCPlayer lgp) {

    }

    @Override
    public void newEpisode(LGGame game, UHCPlayer lgp) {
        this.canSee = true;
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i==1) {
                    lgp.sendMessage(getPrefix() + " " + getPrefixPrivé() + ChatColor.GOLD + " Tu peut voir le role d'un joueur en faisant /lg role see <player>");
                }
                if (!canSee) {
                    cancel();
                }
                if (i == 60 * 5 * 20) {
                    cancel();
                    canSee = false;
                }
                i++;
            }

        }.runTaskTimer(LoupGarouUHC.getPlugin(LoupGarouUHC.class), 1, 1L);
    }

    @Override
    public void killSomeone(LGGame game, UHCPlayer killer, UHCPlayer killed) {

    }

    @Override
    public void killed(LGGame game, UHCPlayer killed, UHCPlayer killer, Location location) {

    }

    @Override
    public void discoverRole(LGGame game, UHCPlayer lgp) {

    }

    @Override
    public void setPower(Boolean p0) {
        this.canSee = p0;
    }

    @Override
    public Boolean hasPower() {
        return canSee;
    }
}
