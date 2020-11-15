package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.exeptions.common.NoUHCPlayerException;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.PlayerGameData;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.ClassicGame;
import fr.radi3nt.uhc.uhc.ClassicPlayerGameData;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.radi3nt.uhc.api.command.CommandUtilis.requirePermission;

public class ManageCommand extends CommandArg {

    @Override
    protected void onCommand(CommandUtilis utilis) throws NoPermissionException, NoArgsException, NoUHCPlayerException {
        if (utilis.checkIfPlayer()) {
            if (requirePermission(utilis.getSender(), "lg.manage", "")) {
                UHCPlayer lgp = UHCPlayer.thePlayer((Player) utilis.getSender());
                if (utilis.requireMinArgs(1)) {
                    switch (utilis.getArgs()[0]) {

                        case "revive":
                            if (utilis.requireMinArgs(2)) {
                                UHCGame game = UHCCore.getGames().get(0);
                                Player target = Bukkit.getPlayerExact(utilis.getArgs()[1]);
                                if (target == null) {
                                    lgp.sendMessage(UHCCore.getPrefix() + ChatColor.DARK_RED + "This player is not online");
                                    return;
                                }

                                UHCPlayer tlgp = UHCPlayer.thePlayer(target);
                                if (tlgp.isInGame() && !tlgp.getGameData().isDead())
                                    return;

                                if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer() != null) {
                                    game = lgp.getGameData().getGame();
                                }
                                if (game instanceof ClassicGame) {
                                    PlayerGameData data = new ClassicPlayerGameData((ClassicGame) game);
                                    target.setGameMode(GameMode.SURVIVAL);
                                    tlgp.setGameData(data);
                                    game.scatter(tlgp);

                                    game.getAlivePlayers().add(tlgp);
                                    game.getDeadPlayers().add(tlgp);
                                    tlgp.setChat(game.getData().getGameChat());
                                } else {
                                    lgp.sendMessage(UHCCore.getPrefix() + ChatColor.DARK_RED + "Please use addon command to join this game: " + game.getData().getDisplayName());
                                }
                            }
                            break;

                        case "scatter":
                            if (utilis.getArgs().length > 1) {
                                UHCPlayer lgT = UHCPlayer.thePlayer(utilis.getArgs()[1]);
                                if (lgT != null) {
                                    if (lgT.isInGame())
                                        lgT.getGameData().getGame().scatter(lgT);
                                }
                            } else {
                                if (lgp.isInGame())
                                    lgp.getGameData().getGame().scatter(lgp);
                            }
                            break;

                        case "name":
                            if (utilis.requireMinArgs(2)) {
                                String name = "";
                                for (int i = 0; i < utilis.getArgs().length; i++) {
                                    if (i == 1) {
                                        name += utilis.getArgs()[i];
                                    }
                                    if (i > 1) {
                                        name += " " + utilis.getArgs()[i];
                                    }
                                }
                                name = ChatColor.translateAlternateColorCodes('&', name);
                                if (name.length() <= 25) {
                                    if (lgp.isInGame()) {
                                        lgp.getGameData().getGame().getData().setDisplayName(name);
                                    } else {
                                        UHCCore.getGames().get(0).getData().setDisplayName(name);
                                    }
                                } else {
                                    lgp.sendMessage(UHCCore.getPrefix() + " To many character !"); //todo trads
                                }
                            }
                            break;

                        case "host":
                            if (utilis.getArgs().length > 1) {
                                UHCPlayer lgT = UHCPlayer.thePlayer(utilis.getArgs()[1]);
                                if (lgT != null) {
                                    if (lgT.isInGame()) {
                                        lgT.getGameData().getGame().getData().setHost(lgT);
                                    } else {
                                        UHCCore.getGames().get(0).getData().setHost(lgT);
                                    }
                                } else {
                                    throw new NoUHCPlayerException();
                                }
                            } else {
                                if (lgp.isInGame()) {
                                    lgp.getGameData().getGame().getData().setHost(null);
                                } else {
                                    UHCCore.getGames().get(0).getData().setHost(null);
                                }
                            }
                            break;

                        case "regen":
                            UHCCore.getGames().remove(0);
                            UHCCore.getGames().add(new ClassicGame());
                            break;

                        case "stop":
                            if (lgp.isInGame()) {
                                lgp.getGameData().getGame().end(new ArrayList<>(), utilis.getArgs().length > 1 ? Boolean.valueOf(utilis.getArgs()[1]) : false);
                            }
                            break;

                        default:
                            utilis.getSender().sendMessage(UHCCore.getPrefix() + ChatColor.RED + " Commande inconnue !");
                            break;
                    }
                }
            }
        } else {
            throw new NoUHCPlayerException();
        }
    }

    @Override
    protected List<String> tabComplete(CommandUtilis utilis) {
        return null;
    }
}
