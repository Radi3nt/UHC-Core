package fr.radi3nt.loupgarouuhc.classes.game;

import fr.radi3nt.loupgarouuhc.classes.chats.DeadChat;
import fr.radi3nt.loupgarouuhc.classes.chats.GameChat;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;

public class LGGameData {

    private LGPlayer host;

    private Logger logChat;
    private DeadChat deathChat;
    private GameChat gameChat;

    private String name;
    private boolean practice = false;
    private String displayName = "&4Loup Garou UHC";

    public LGPlayer getHost() {
        return host;
    }

    public void setHost(LGPlayer host) {
        this.host = host;
    }

    public Logger getLogChat() {
        return logChat;
    }

    public void setLogChat(Logger logChat) {
        this.logChat = logChat;
    }

    public DeadChat getDeathChat() {
        return deathChat;
    }

    public void setDeathChat(DeadChat deathChat) {
        this.deathChat = deathChat;
    }

    public GameChat getGameChat() {
        return gameChat;
    }

    public void setGameChat(GameChat gameChat) {
        this.gameChat = gameChat;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isPractice() {
        return practice;
    }

    public void setPractice(boolean practice) {
        this.practice = practice;
    }
}
