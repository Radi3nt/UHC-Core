package fr.radi3nt.loupgarouuhc.classes.roles;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public abstract class Role {

    private LGGame game;
    private WinType winType;
    private RoleType roleType;

    public abstract RoleSort getRoleSort();

    public WinType getWinType() {
        return this.winType;
    }
    public void setWinType(WinType winType) {
        this.winType=winType;
    }
    public RoleType getRoleType() {
        return this.roleType;
    }
    public void setRoleType(RoleType roleType) {
        this.roleType=roleType;
    }
    public String getName() {
        return this.getRoleSort().name;
    }
    public String getShortDescription() {
        return this.getRoleSort().shortDescription;
    }
    public String getDescription() {
        return this.getRoleSort().roleDescription;
    }


    public Role(LGGame game) {
        this.game = game;
        this.winType = this.getRoleSort().winType;
        this.roleType = this.getRoleSort().roleType;
    }

    public abstract void OnNight(LGGame game, LGPlayer lgp);
    public abstract void OnDay(LGGame game, LGPlayer lgp);
    public abstract void OnNewEpisode(LGGame game, LGPlayer lgp);
    public abstract void OnKillSomeone(LGGame game, LGPlayer killer, LGPlayer killed);
    public abstract void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location);
    public abstract void OnDiscoverRole(LGGame game, LGPlayer lgp);

    public void join(LGPlayer player, boolean sendMessage) {
        System.out.println(player.getName() + " est " + this.getName());
        player.setRole(this);
        if (sendMessage) {
            player.sendTitle("§6Tu es " + this.getName(), "§e" + this.getShortDescription(), 10, 200, 10);
            player.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " Tu es " + this.getName() + ".");
            player.sendMessage(ChatColor.BLUE + this.getDescription());
        }
    }

    public LGGame getGame() {
        return game;
    }

    public void setGame(LGGame game) {
        this.game = game;
    }
}
