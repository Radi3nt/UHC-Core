package fr.radi3nt.loupgarouuhc.classes.player;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;

public class PlayerGameData {

    private LGGame game;
    private Role role;

    private boolean dead = false;
    private boolean canVote = false;
    private boolean canBeRespawned = false;
    private boolean infected = false;
    private LGPlayer couple;
    private LGPlayer killer;
    private Integer kills = 0;

    public PlayerGameData(LGGame game) {
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
    }

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

    public LGPlayer getCouple() {
        return couple;
    }

    public void setCouple(LGPlayer couple) {
        this.couple = couple;
    }

    public LGPlayer getKiller() {
        return killer;
    }

    public void setKiller(LGPlayer killer) {
        this.killer = killer;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }
}
