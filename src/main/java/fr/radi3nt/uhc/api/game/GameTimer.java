package fr.radi3nt.uhc.api.game;

import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioUtils;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class GameTimer extends BukkitRunnable {

    protected final UHCGame game;
    protected int ticks = 0;

    public GameTimer(UHCGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        try {
            _run();
        } catch (Exception e) {
            Logger.getGeneralLogger().log(e);
        }
    }

    public abstract void _run();

    public int getTicks() {
        return ticks;
    }

    public abstract void setTicks(int ticks);

    public UHCGame getGame() {
        return game;
    }
}
