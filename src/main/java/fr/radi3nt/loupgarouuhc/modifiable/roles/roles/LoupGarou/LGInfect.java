package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleSort;

public class LGInfect extends LoupGarou {

    public boolean hasrespwaned = false;


    public RoleSort getRoleSort() {
        return RoleSort.LG_INFECTE;
    }


    public LGInfect(LGGame game) {
        super(game);
    }


}
