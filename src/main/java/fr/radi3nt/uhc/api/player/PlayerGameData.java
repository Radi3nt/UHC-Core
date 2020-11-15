package fr.radi3nt.uhc.api.player;

import fr.radi3nt.uhc.api.game.UHCGame;

public interface PlayerGameData {

    UHCGame getGame();

    UHCPlayer getKiller();

    void setKiller(UHCPlayer killer);

    boolean isDead();

    void setDead(boolean dead);

    int getKills();

    void setKills(int kills);

}
