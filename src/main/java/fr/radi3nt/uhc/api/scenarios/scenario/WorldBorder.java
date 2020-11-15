package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioCommand;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class WorldBorder extends Scenario {

    private final HashMap<Integer, Integer> worldBorder = new HashMap<>();
    boolean working = true;
    private int lastTick = 0;

    public WorldBorder(UHCGame game) {
        super(game);
        //--
        worldBorder.put(0, game.getParameters().getBaseRadius());
        //--

        worldBorder.put(2 * 20 * 60 * 20, 1000);
        worldBorder.put(6 * 20 * 60 * 20, 300);
        worldBorder.put(8 * 20 * 60 * 20, 100);
        worldBorder.put(9 * 20 * 60 * 20, 50);
    }

    public static String getName() {
        return "WorldBorder";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.BEDROCK);
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        super.tick(gameTimer, tick);
        if (gameTimer.getGame() == game) {
            if (isActive()) {
                if (gameTimer.getGame().getState() == GameState.PLAYING) {
                    lastTick = tick;
                    if (working) {
                        update(tick);
                    }
                }
            }
        }
    }

    private void update(int tick) {
        Integer min = 0;
        for (Map.Entry<Integer, Integer> minKey : worldBorder.entrySet()) {
            if (minKey.getKey() <= tick && minKey.getKey() > min) {
                min = minKey.getKey();
            }
        }
        for (Map.Entry<Integer, Integer> maxKey : worldBorder.entrySet()) {
            if (maxKey.getKey() >= tick && !min.equals(maxKey.getKey())) {
                float coef = ((float) maxKey.getValue() - worldBorder.getOrDefault(min, 0)) / ((float) maxKey.getKey() - min);
                double worldBorderSize = worldBorder.getOrDefault(min, 0) + (coef * (tick - min));
                game.getGameSpawn().getWorld().getWorldBorder().setSize(worldBorderSize * 2);
                game.setRadius((int) worldBorderSize);
                break;
            }
        }
    }

    @ScenarioCommand
    public void onCommand(CommandUtilis command) {
        if (isActive()) {
            try {
                if (command.executeCommand("uhc.wb.add", "uhc.worldborder.add", 1, CommandUtilis.Checks.PLAYER)) {
                    UHCPlayer lgp = UHCPlayer.thePlayer((Player) command.getSender());
                    if (UHCCore.getGames().isEmpty())
                        return;
                    UHCGame game = UHCCore.getGames().get(0);
                    if (lgp.isInGame())
                        game = lgp.getGameData().getGame();
                    if (game.equals(this.game)) {
                        int integer = 0;
                        try {
                            integer = Integer.parseInt(command.getArgs()[2]);
                        } catch (NumberFormatException e) {
                            return;
                        }
                        removeSize(integer);
                    }
                }
                if (command.executeCommand("uhc.wb.remove", "uhc.worldborder.remove", 1, CommandUtilis.Checks.PLAYER)) {
                    UHCPlayer lgp = UHCPlayer.thePlayer((Player) command.getSender());
                    if (UHCCore.getGames().isEmpty())
                        return;
                    UHCGame game = UHCCore.getGames().get(0);
                    if (lgp.isInGame())
                        game = lgp.getGameData().getGame();
                    if (game.equals(this.game)) {
                        int integer = 0;
                        try {
                            integer = Integer.parseInt(command.getArgs()[2]);
                        } catch (NumberFormatException e) {
                            return;
                        }
                        addSize(integer);
                    }
                }
                if (command.executeCommand("uhc.wb.set", "uhc.worldborder.set", 1, CommandUtilis.Checks.PLAYER)) {
                    UHCPlayer lgp = UHCPlayer.thePlayer((Player) command.getSender());
                    if (UHCCore.getGames().isEmpty())
                        return;
                    UHCGame game = UHCCore.getGames().get(0);
                    if (lgp.isInGame())
                        game = lgp.getGameData().getGame();
                    if (game.equals(this.game)) {
                        int integer = 0;
                        try {
                            integer = Integer.parseInt(command.getArgs()[2]);
                        } catch (NumberFormatException e) {
                            return;
                        }
                        setSize(lastTick, integer);
                    }
                }
                if (command.executeCommand("uhc.wb.start", "uhc.worldborder.start", 0, CommandUtilis.Checks.PLAYER)) {
                    UHCPlayer lgp = UHCPlayer.thePlayer((Player) command.getSender());
                    if (UHCCore.getGames().isEmpty())
                        return;
                    UHCGame game = UHCCore.getGames().get(0);
                    if (lgp.isInGame())
                        game = lgp.getGameData().getGame();
                    if (game.equals(this.game)) {
                        start();
                    }
                }
                if (command.executeCommand("uhc.wb.stop", "uhc.worldborder.stop", 0, CommandUtilis.Checks.PLAYER)) {
                    UHCPlayer lgp = UHCPlayer.thePlayer((Player) command.getSender());
                    if (UHCCore.getGames().isEmpty())
                        return;
                    UHCGame game = UHCCore.getGames().get(0);
                    if (lgp.isInGame())
                        game = lgp.getGameData().getGame();
                    if (game.equals(this.game)) {
                        stop();
                    }
                }
            } catch (NoPermissionException | NoArgsException e) {

            }
        }
    }

    private void addSize(int size) {
        HashMap<Integer, Integer> hashMap = (HashMap<Integer, Integer>) worldBorder.clone();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : hashMap.entrySet()) {
            worldBorder.put(integerIntegerEntry.getKey(), integerIntegerEntry.getValue() + size);
        }
    }

    private void removeSize(int size) {
        HashMap<Integer, Integer> hashMap = (HashMap<Integer, Integer>) worldBorder.clone();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : hashMap.entrySet()) {
            worldBorder.put(integerIntegerEntry.getKey(), integerIntegerEntry.getValue() - size);
        }

    }

    private void setSize(int ticks, int size) {
        worldBorder.put(ticks, size);
    }

    private void stop() {
        working = false;
        update(lastTick);
    }

    private void start() {
        working = true;
    }


}
