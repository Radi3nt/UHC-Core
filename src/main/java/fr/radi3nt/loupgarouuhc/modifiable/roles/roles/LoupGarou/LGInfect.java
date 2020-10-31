package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LGRoleIdentity;

public class LGInfect extends LoupGarou {

    public boolean hasrespwaned = false;


    public static RoleIdentity getStaticRoleIdentity() {
        return new LGRoleIdentity("LGInfect", LoupGarou.getStaticRoleIdentity().getRoleItems(), LoupGarou.getStaticRoleIdentity().getPotionEffectsDay(), LoupGarou.getStaticRoleIdentity().getPotionEffectsNight(), 20).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    public LGInfect(LGGame game) {
        super(game);
    }


}
