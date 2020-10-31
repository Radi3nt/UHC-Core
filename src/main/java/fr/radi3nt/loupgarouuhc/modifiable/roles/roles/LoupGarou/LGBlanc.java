package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LGRoleIdentity;

public class LGBlanc extends LoupGarou {

    public LGBlanc(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new LGRoleIdentity("LGBlanc", LoupGarou.getStaticRoleIdentity().getRoleItems(), LoupGarou.getStaticRoleIdentity().getPotionEffectsDay(), LoupGarou.getStaticRoleIdentity().getPotionEffectsNight(), 30).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

}
