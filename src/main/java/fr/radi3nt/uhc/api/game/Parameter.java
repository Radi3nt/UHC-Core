package fr.radi3nt.uhc.api.game;

import org.bukkit.Location;

public interface Parameter {

    Location getSpawn();

    Location getGameSpawn();

    Integer getBaseRadius();

    Boolean recreateMapAfterUse();

    DisconnectParameters getDisconnectParameters();

}
