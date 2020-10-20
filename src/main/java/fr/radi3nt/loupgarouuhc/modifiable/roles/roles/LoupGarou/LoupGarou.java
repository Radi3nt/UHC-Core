package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleSort;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoupGarou extends Role {

    public LoupGarou(LGGame game) {
        super(game);
    }


    public RoleSort getRoleSort() {
        return RoleSort.LOUP_GAROU;
    }

    public void OnNight(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0, true, false), true);
    }

    
    public void OnDay(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
    }

    
    public void OnKillSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {
        killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60*20, 0, true, false), true);
        killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 0, true, false), true);
    }

    @Override
    public void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {

    }


    public void OnDiscoverRole(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, true, false), true);
    }

}
