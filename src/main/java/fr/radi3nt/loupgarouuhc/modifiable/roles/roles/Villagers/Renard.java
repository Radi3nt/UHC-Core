package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.VillagerRoleIdentity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefix;
import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.getPrefixPrivé;

public class Renard extends Role {

    private int time = 0;
    private boolean canSee;
    private int radius = 20;

    public Renard(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        ArrayList<PotionEffect> potionEffects = new ArrayList<>();
        ArrayList<PotionEffect> potionEffects2 = new ArrayList<>();
        potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999 * 20, 0, true, false));
        potionEffects2.add(new PotionEffect(PotionEffectType.SPEED, 999999 * 20, 0, true, false));
        return new VillagerRoleIdentity("Renard", new ArrayList<>(), potionEffects, new ArrayList<>(), potionEffects2, 20).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    public void night(LGGame game, LGPlayer lgp) {
        if (time < 3) {
            lgp.sendMessage(getPrefix() + " " + getPrefixPrivé() + ChatColor.GOLD + " Tu peut voir si un joueur est loup garou ou pas en faisant /lg role see <player>, tu pourras flairer un joueur encore " + ChatColor.YELLOW + (3 - time) + ChatColor.GOLD + " fois");
            this.canSee = true;
        }
    }

    @Override
    public void day(LGGame game, LGPlayer lgp) {
        canSee = false;
    }

    @Override
    public void newEpisode(LGGame game, LGPlayer lgp) {

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

    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }

    public boolean canSee() {
        return canSee;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
