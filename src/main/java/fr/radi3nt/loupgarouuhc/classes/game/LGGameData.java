package fr.radi3nt.loupgarouuhc.classes.game;

import fr.radi3nt.loupgarouuhc.classes.chats.Chat;
import fr.radi3nt.loupgarouuhc.classes.chats.DeadChat;
import fr.radi3nt.loupgarouuhc.classes.chats.GameChat;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;

public class LGGameData {

    private LGPlayer host;
    private Chat logChat;
    private DeadChat deathChat;
    private GameChat gameChat;
    private String name;
    private String displayName = "&4Loup Garou UHC";

    public LGPlayer getHost() {
        return host;
    }

    public void setHost(LGPlayer host) {
        this.host = host;
    }

    public Chat getLogChat() {
        return logChat;
    }

    public void setLogChat(Chat logChat) {
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
}
