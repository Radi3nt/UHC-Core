package fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers.Villager;
import org.bukkit.ChatColor;

import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;

public class LGFeutre extends LoupGarou {

    public Role affichage;

    public LGFeutre(LGGame game) {
        super(game);
    }

    public RoleSort getRoleSort() {
        return RoleSort.LG_FEUTRE;
    }

    @Override
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        Role role = null;
        int i = 0;
        while (role==null || role.getRoleSort()==RoleSort.LG_FEUTRE) {
            role = searchRole();
            i++;
            if (i==2048) {
                role=new Villager(lgp.getGame());
                break;
            }
        }
        affichage=role;
        super.OnNewEpisode(game, lgp);
        lgp.sendMessage(LoupGarouUHC.prefix + ChatColor.GOLD + " Ton role d'affichage est " + ChatColor.RED + affichage.getName(lgp.getLanguage()));
    }

    public Role searchRole () {
        try {
            return getGame().getRoles().get(new SecureRandom().nextInt(getGame().getRoles().size())).getClass().getConstructor(LGGame.class).newInstance(getGame());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
