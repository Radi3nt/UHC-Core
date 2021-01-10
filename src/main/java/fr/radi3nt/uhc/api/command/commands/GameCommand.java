package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.exeptions.common.NoUHCPlayerException;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.GameType;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static fr.radi3nt.uhc.api.command.CommandUtilis.requirePermission;

public class GameCommand implements CommandArg {

    @Override
    public void onCommand(CommandUtilis utilis) throws NoArgsException, NoPermissionException, NoUHCPlayerException {
        requirePermission(utilis.getSender(), "uhc.game.manage", "");
        if (utilis.requireMinArgs(2)) {
            GameType game = null;
            for (GameType registeredGame : UHCCore.getRegisteredGames()) {
                if (utilis.getArgs()[0].equalsIgnoreCase(registeredGame.getID())) {
                    game=registeredGame;
                }
            }
            switch (utilis.getArgs()[1]) {
                case "add":
                    try {
                        UHCCore.getGameQueue().add(game.getGameClass().getConstructor().newInstance());
                    } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        Logger.getGeneralLogger().log(e);
                    }
                    break;
                case "select":
                    for (UHCGame uhcGame : UHCCore.getGameQueue()) {
                        if (uhcGame.getClass().equals(game.getGameClass()) && uhcGame.getState()==GameState.LOBBY) {
                            UHCCore.getGameQueue().remove(uhcGame);
                            UHCCore.getGameQueue().add(0, uhcGame);
                            return;
                        }
                    }
                    try {
                        UHCCore.getGameQueue().add(0, game.getGameClass().getConstructor().newInstance());
                    } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        Logger.getGeneralLogger().log(e);
                    }
                    break;
                case "join":
                    for (UHCGame uhcGame : UHCCore.getGameQueue()) {
                        if (game.getGameClass().equals(uhcGame.getClass()) && uhcGame.getState()== GameState.LOBBY) {
                            UHCPlayer player = UHCPlayer.thePlayer((Player) utilis.getSender());
                            if (requirePermission(player.getPlayer(), "uhc.join", "")) {
                                if (utilis.getArgs().length > 2) {
                                    if (requirePermission(player.getPlayer(), "uhc.join.others", ""))
                                    if (Bukkit.getPlayerExact(utilis.getArgs()[2]) != null)
                                        player = UHCPlayer.thePlayer(Bukkit.getPlayerExact(utilis.getArgs()[2]));
                                    else throw new NoUHCPlayerException();
                                }
                                join(player, uhcGame);
                            }
                            break;
                        }
                    }

            }
        }
    }

    private void join(UHCPlayer player, UHCGame game) {
        if (game.getState()==GameState.LOBBY) {
                    if (!game.getWaitQueue().contains(player)) {
                        game.join(player);
                        player.sendIdMessage("commands.join");
                    }
            }
    }

    @Override
    public List<String> tabComplete(CommandUtilis utilis) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (utilis.getArgs().length==1) {
            for (GameType registeredGame : UHCCore.getRegisteredGames()) {
                arrayList.add(registeredGame.getID());
            }
            arrayList.removeIf(s1 -> !s1.startsWith(utilis.getArgs()[0]));
        }
        if (utilis.getArgs().length==2) {
            arrayList.add("add");
            arrayList.add("select");
            arrayList.add("join");
            arrayList.removeIf(s1 -> !s1.startsWith(utilis.getArgs()[1]));
        }
        return arrayList;
    }
}
