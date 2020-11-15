package fr.radi3nt.uhc.uhc;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.PlayerGameData;
import fr.radi3nt.uhc.api.player.UHCPlayer;

public class ClassicPlayerGameData implements PlayerGameData {

    private final ClassicGame game;
    private boolean dead = false;
    private int kills = 0;
    private UHCPlayer killer;

    public ClassicPlayerGameData(ClassicGame game) {
        this.game = game;
    }

    @Override
    public UHCGame getGame() {
        return game;
    }

    @Override
    public UHCPlayer getKiller() {
        return killer;
    }

    @Override
    public void setKiller(UHCPlayer killer) {
        this.killer = killer;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
    }

}
