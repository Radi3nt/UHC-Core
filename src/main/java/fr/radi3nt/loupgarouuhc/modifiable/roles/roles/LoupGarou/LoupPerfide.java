package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleSort;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoupPerfide extends LoupGarou {

    public LoupPerfide(LGGame game) {
        super(game);
    }

    @Override
    public RoleSort getRoleSort() {
        return RoleSort.LOUP_PERFIDE;
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 1, true, false), true);
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 9999999, 1, true, false), true);
    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        lgp.getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
    }
}
