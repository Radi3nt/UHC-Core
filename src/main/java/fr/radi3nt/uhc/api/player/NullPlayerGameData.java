package fr.radi3nt.uhc.api.player;

import fr.radi3nt.uhc.api.game.UHCGame;

public class NullPlayerGameData implements PlayerGameData {

    @Override
    public UHCGame getGame() {
        return null;
    }

    @Override
    public UHCPlayer getKiller() {
        return null;
    }

    @Override
    public void setKiller(UHCPlayer killer) {

    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public void setDead(boolean dead) {

    }

    @Override
    public int getKills() {
        return 0;
    }

    @Override
    public void setKills(int kills) {

    }
}
