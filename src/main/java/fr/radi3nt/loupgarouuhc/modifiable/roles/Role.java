package fr.radi3nt.loupgarouuhc.modifiable.roles;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.lang.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public abstract class Role {

    private static final HashMap<RoleIdentity, Constructor<? extends Role>> roleLinkByStringKey = new HashMap<>();

    private LGGame game;
    private WinType winType;
    private RoleType roleType;

    public Role(LGGame game) {
        this.game = game;
        this.winType = this.getRoleIdentity().getWinType();
        this.roleType = this.getRoleIdentity().getRoleType();
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return null;
    }

    public WinType getWinType() {
        return this.winType;
    }

    public void setWinType(WinType winType) {
        this.winType = winType;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public static HashMap<RoleIdentity, Constructor<? extends Role>> getRoleLinkByStringKey() {
        return roleLinkByStringKey;
    }

    public abstract RoleIdentity getRoleIdentity();

    public String getName(Languages languages) {
        return this.getRoleIdentity().getName(languages);
    }

    public String getShortDescription(Languages languages) {
        return this.getRoleIdentity().getShortDescription(languages);
    }

    public abstract void OnNight(LGGame game, LGPlayer lgp);

    public abstract void OnDay(LGGame game, LGPlayer lgp);

    public abstract void OnNewEpisode(LGGame game, LGPlayer lgp);

    public abstract void OnKillSomeone(LGGame game, LGPlayer killer, LGPlayer killed);

    public abstract void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location);

    public abstract void OnDiscoverRole(LGGame game, LGPlayer lgp);

    public void join(LGPlayer player, boolean sendMessage) {
        System.out.println(player.getName() + " est " + this.getName(player.getLanguage()));
        player.getGameData().setRole(this);
        if (sendMessage) {
            player.sendTitle((player.getLanguage().getMessage("joinRoleTitleTitle", player)), (player.getLanguage().getMessage("joinRoleTitleSubtitle", player)), 10, 200, 10);
            player.sendMessage(player.getLanguage().getMessage("joinRoleMessage", player));
        }
    }

    public LGGame getGame() {
        return game;
    }

    public void setGame(LGGame game) {
        this.game = game;
    }

    public String getDescription(Languages languages) {
        return this.getRoleIdentity().getRoleDescription(languages);
    }
}
