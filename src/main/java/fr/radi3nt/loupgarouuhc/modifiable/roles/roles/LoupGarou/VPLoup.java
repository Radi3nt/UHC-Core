package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LGRoleIdentity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class VPLoup extends LoupGarou {

    public VPLoup(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        List<PotionEffect> night = LoupGarou.getStaticRoleIdentity().getPotionEffectsNight();
        night.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1, true, false));

        return new LGRoleIdentity("VPLoup", LoupGarou.getStaticRoleIdentity().getRoleItems(), LoupGarou.getStaticRoleIdentity().getPotionEffectsDay(), night, 20).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }


}
