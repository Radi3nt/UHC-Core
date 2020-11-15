package fr.radi3nt.loupgarouuhc.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.LGRoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.attributes.Power;

public class LGInfect extends LoupGarou implements Power {

    private boolean nonRespawned = true;


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


    @Override
    public void setPower(Boolean p0) {
        nonRespawned =p0;
    }

    @Override
    public Boolean hasPower() {
        return nonRespawned;
    }
}
