package fr.radi3nt.loupgarouuhc.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.LGRoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.roles.Villagers.Villager;

import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;

public class LGFeutre extends LoupGarou {

    public Role affichage;

    public LGFeutre(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new LGRoleIdentity("LGFeutre", LoupGarou.getStaticRoleIdentity().getRoleItems(), LoupGarou.getStaticRoleIdentity().getPotionEffectsDay(), LoupGarou.getStaticRoleIdentity().getPotionEffectsNight(), 20).getIdentity();
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    public void newEpisode(LGGame game, UHCPlayer lgp) {
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
        affichage = role;
        super.newEpisode(game, lgp);
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
