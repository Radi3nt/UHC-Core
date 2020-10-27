package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers.Villager;

import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;

public class LGFeutre extends LoupGarou {

    public Role affichage;

    public LGFeutre(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("LoupFeutre", WinType.LOUP_GAROU, RoleType.LOUP_GAROU);
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        Role role = null;
        int i = 0;
        while (role == null || role.getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity())) {
            role = searchRole();
            i++;
            if (i == 2048) {
                role = new Villager(lgp.getGameData().getGame());
                break;
            }
        }
        affichage=role;
        super.OnNewEpisode(game, lgp);
        lgp.sendMessage(lgp.getLanguage().getMessage("roleFeutreAffichage", lgp).replace("%affichageName%", affichage.getName(lgp.getLanguage())));
    }

    private Role searchRole() {
        try {
            return getGame().getRoles().get(new SecureRandom().nextInt(getGame().getRoles().size())).getClass().getConstructor(LGGame.class).newInstance(getGame());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
