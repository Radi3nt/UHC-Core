package fr.radi3nt.uhc.api.player;

import fr.radi3nt.uhc.api.game.UHCGame;

public class PlayerGameData {

    private UHCGame game;
    private PlayerState playerState = PlayerState.NOT_PLAYING;

    public PlayerGameData(UHCGame game) {
        this.game = game;
    }

    public UHCGame getGame() {
        return game;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState=playerState;
    }

}
