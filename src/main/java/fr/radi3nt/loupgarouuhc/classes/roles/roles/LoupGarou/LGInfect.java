package fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;

public class LGInfect extends LoupGarou {

    public boolean hasrespwaned = false;

    public String getName() {
        return RoleSort.LG_INFECTE.name;
    }


    public RoleSort getRoleSort() {
        return RoleSort.LG_INFECTE;
    }


    public LGInfect(LGGame game) {
        super(game);
    }


}
