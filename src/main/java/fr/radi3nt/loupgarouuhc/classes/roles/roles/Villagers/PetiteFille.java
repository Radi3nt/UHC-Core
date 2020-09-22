package fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PetiteFille extends Role {

    public PetiteFille(LGGame game) {
        super(game);
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, true, false), true);
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 999999, 1, true, false), true);

    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        lgp.getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
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
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, true, false), true);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.PETITE_FILLE;
    }



}


