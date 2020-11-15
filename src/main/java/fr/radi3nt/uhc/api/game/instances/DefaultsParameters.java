package fr.radi3nt.uhc.api.game.instances;

import fr.radi3nt.uhc.api.game.DisconnectParameters;
import fr.radi3nt.uhc.api.game.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DefaultsParameters implements Parameter {

    @Override
    public Location getSpawn() {
        return Bukkit.getWorlds().get(0).getSpawnLocation();
    }

    @Override
    public Location getGameSpawn() {
        return Bukkit.getWorlds().get(0).getSpawnLocation();
    }

    @Override
    public Integer getBaseRadius() {
        return 1000;
    }

    @Override
    public DisconnectParameters getDisconnectParameters() {
        return new DisconnectParameters();
    }
}
