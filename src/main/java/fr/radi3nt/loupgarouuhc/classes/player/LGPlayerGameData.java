package fr.radi3nt.loupgarouuhc.classes.player;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import fr.radi3nt.loupgarouuhc.roles.WinType;
import fr.radi3nt.uhc.api.player.PlayerGameData;
import fr.radi3nt.uhc.api.player.UHCPlayer;

public class LGPlayerGameData implements PlayerGameData {

    private LGGame game;
    private Role role;

    private boolean dead = false;
    private boolean canVote = false;
    private boolean canBeRespawned = false;
    private UHCPlayer couple;
    private UHCPlayer killer;
    private Integer kills = 0;

    public LGPlayerGameData(LGGame game) {
        this.game = game;
    }

    public LGGame getGame() {
        return game;
    }

    public void setGame(LGGame game) {
        this.game = game;
    }

    public boolean hasRole() {
        return role != null && game != null;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public RoleType getRoleType() {
        return role.getRoleType();
    }

    public RoleIdentity getRoleIdentity() {
        return role.getRoleIdentity();
    }

    public WinType getRoleWinType() {
        return role.getWinType();
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean canVote() {
        return canVote;
    }//todo this should go in PlayerVote.class

    public void setCanVote(boolean canVote) {
        this.canVote = canVote;
    }

    public boolean canBeRespawned() {
        return canBeRespawned;
    }

    public void setCanBeRespawned(boolean canBeRespawned) {
        this.canBeRespawned = canBeRespawned;
    }

    public boolean isInCouple() {
        return couple != null;
    }

    public UHCPlayer getCouple() {
        return couple;
    }

    public void setCouple(UHCPlayer couple) {
        this.couple = couple;
    }

    public UHCPlayer getKiller() {
        return killer;
    }

    public void setKiller(UHCPlayer killer) {
        this.killer = killer;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

}
