package fr.radi3nt.loupgarouuhc.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.LGRoleIdentity;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class LoupGarou extends Role {

    public LoupGarou(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        ArrayList<PotionEffect> potionEffects = new ArrayList<>();
        potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0, true, false));

        ArrayList<PotionEffect> potionEffectsNight = new ArrayList<>();
        potionEffectsNight.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0, true, false));

        return new LGRoleIdentity("LoupGarou", new ArrayList<>(), potionEffects, new ArrayList<>(), potionEffectsNight, 20).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    public void night(LGGame game, UHCPlayer lgp) {

    }


    public void day(LGGame game, UHCPlayer lgp) {

    }


    public void newEpisode(LGGame game, UHCPlayer lgp) {

    }


    public void killSomeone(LGGame game, UHCPlayer killer, UHCPlayer killed) {
        killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60 * 20, 0, true, false), true);
        killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 0, true, false), true);
    }

    @Override
    public void killed(LGGame game, UHCPlayer killed, UHCPlayer killer, Location location) {

    }


    public void discoverRole(LGGame game, UHCPlayer lgp) {

    }

}