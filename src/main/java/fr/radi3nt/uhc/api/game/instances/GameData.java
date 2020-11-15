package fr.radi3nt.uhc.api.game.instances;

import fr.radi3nt.uhc.api.chats.DeadChat;
import fr.radi3nt.uhc.api.chats.GameChat;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.ChatColor;

public class GameData {

    private UHCPlayer host;

    private Logger logChat;
    private DeadChat deathChat;
    private GameChat gameChat;

    private String name;
    private boolean practice = false;
    private String displayName = "&4&lUHC";

    public UHCPlayer getHost() {
        return host;
    }

    public void setHost(UHCPlayer host) {
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
        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public void setDisplayName(String displayName) {
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public boolean isPractice() {
        return practice;
    }

    public void setPractice(boolean practice) {
        this.practice = practice;
    }
}
