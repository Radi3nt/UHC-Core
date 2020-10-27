package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;

public class LGInfect extends LoupGarou {

    public boolean hasrespwaned = false;


    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("LGInfect", WinType.LOUP_GAROU, RoleType.LOUP_GAROU);
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    public LGInfect(LGGame game) {
        super(game);
    }


}
