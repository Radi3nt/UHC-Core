package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.*;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.GameInformation;
import fr.radi3nt.uhc.api.player.PlayerGameData;
import fr.radi3nt.uhc.api.player.PlayerState;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.ClassicGame;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.radi3nt.uhc.api.command.CommandUtilis.requirePermission;

public class ManageCommand implements CommandArg {

    @Override
    public void onCommand(CommandUtilis utilis) throws NoPermissionException, NoArgsException, NoUHCPlayerException, InvalidArgsException, NoCommandException {
        if (utilis.checkIfPlayer()) {
            if (requirePermission(utilis.getSender(), "uhc.manage", "")) {
                UHCPlayer lgp = UHCPlayer.thePlayer((Player) utilis.getSender());
                if (utilis.requireMinArgs(1)) {
                    switch (utilis.getArgs()[0]) {

                        case "revive":
                            if (utilis.requireMinArgs(2)) {
                                UHCGame game = UHCCore.getGameQueue().get(0);
                                Player target = Bukkit.getPlayerExact(utilis.getArgs()[1]);
                                if (target == null) {
                                    lgp.sendMessage(UHCCore.getPrefix() + ChatColor.DARK_RED + "This player is not online");
                                    return;
                                }

                                UHCPlayer tlgp = UHCPlayer.thePlayer(target);
                                if (tlgp.isInGame())
                                    return;

                                if (lgp.isPlaying() && lgp.getGameData().getGame().getState() == GameState.PLAYING) {
                                    game = lgp.getGameData().getGame();
                                }
                                if (game instanceof ClassicGame) {
                                    target.setGameMode(GameMode.SURVIVAL);
                                    tlgp.setGameData(new PlayerGameData(game));
                                    tlgp.getGameData().setPlayerState(PlayerState.ALIVE);
                                    tlgp.setGameInformation(new GameInformation());
                                    game.scatter(tlgp);

                                    game.forceJoin(tlgp);
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
                                    if (lgT.isPlaying())
                                        lgT.getGameData().getGame().scatter(lgT);
                                }
                            } else {
                                if (lgp.isPlaying())
                                    lgp.getGameData().getGame().scatter(lgp);
                            }
                            break;

                        case "time":
                            if (utilis.requireMinArgs(3)) {
                                if (lgp.isPlaying()) {
                                    int time = 0;
                                    try {
                                        time = Integer.parseInt(utilis.getArgs()[2]);
                                    } catch (NumberFormatException e) {
                                        throw new InvalidArgsException("should be a number");
                                    }
                                    switch (utilis.getArgs()[1]) {
                                        case "add":
                                            lgp.getGameData().getGame().getGameTimer().setTicks(lgp.getGameData().getGame().getGameTimer().getTicks() + time*60*20);
                                            break;
                                        case "set":
                                            lgp.getGameData().getGame().getGameTimer().setTicks(time*60*20);
                                            break;
                                        case "minus":
                                            lgp.getGameData().getGame().getGameTimer().setTicks(lgp.getGameData().getGame().getGameTimer().getTicks() - time*60*20);
                                            break;
                                        default:
                                            throw new NoCommandException("need to be: add, set or minus"); //todo auto complete player
                                    }
                                }
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
                                    if (lgp.isPlaying()) {
                                        lgp.getGameData().getGame().getData().setDisplayName(name);
                                    } else {
                                        UHCCore.getGameQueue().get(0).getData().setDisplayName(name);
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
                                    if (lgT.isPlaying()) {
                                        lgT.getGameData().getGame().getData().setHost(lgT);
                                    } else {
                                        UHCCore.getGameQueue().get(0).getData().setHost(lgT);
                                    }
                                } else {
                                    throw new NoUHCPlayerException();
                                }
                            } else {
                                if (lgp.isPlaying()) {
                                    lgp.getGameData().getGame().getData().setHost(null);
                                } else {
                                    UHCCore.getGameQueue().get(0).getData().setHost(null);
                                }
                            }
                            break;

                        case "regen":
                            UHCCore.getGameQueue().remove(0);
                            UHCCore.getGameQueue().add(new ClassicGame());
                            break;

                        case "stop":
                            if (lgp.isPlaying()) {
                                lgp.getGameData().getGame().end(new ArrayList<>(), utilis.getArgs().length > 1 && Boolean.parseBoolean(utilis.getArgs()[1]));
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
    public List<String> tabComplete(CommandUtilis utilis) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (utilis.getArgs().length==1) {
            arrayList.add("revive");
            arrayList.add("time add");
            arrayList.add("time set");
            arrayList.add("time minus");
            arrayList.add("stop");
            arrayList.add("regen");
            arrayList.add("host");
            arrayList.add("name");
            arrayList.add("scatter");
            arrayList.removeIf(s -> !s.startsWith(utilis.getArgs()[0]));
        }
        return arrayList;
    }
}
