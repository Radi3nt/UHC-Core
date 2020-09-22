package fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VPLoup extends LoupGarou {

    public VPLoup(LGGame game) {
        super(game);
    }


    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, true, false), true);
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0, true, false), true);

    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        lgp.getPlayer().removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.VP_LOUP;
    }

}
