package fr.radi3nt.loupgarouuhc.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.uhc.api.gui.guis.MainGUI;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.scenarios.scenario.vote.Vote;
import fr.radi3nt.uhc.api.lang.lang.Languages;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.loupgarouuhc.classes.message.messages.NoPermission;
import fr.radi3nt.uhc.api.player.npc.NPC;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayerGameData;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import fr.radi3nt.loupgarouuhc.roles.WinType;
import fr.radi3nt.loupgarouuhc.roles.roles.LoupGarou.LGFeutre;
import fr.radi3nt.loupgarouuhc.roles.roles.LoupGarou.LGInfect;
import fr.radi3nt.loupgarouuhc.roles.roles.Solo.Cupidon;
import fr.radi3nt.loupgarouuhc.roles.roles.Villagers.*;
import fr.radi3nt.uhc.api.scenarios.scenario.vote.PlayerVote;
import fr.radi3nt.uhc.api.utilis.Maths;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class LGcommands implements CommandExecutor {
    private static ArrayList<Location> generateSphere(Location center, int radius) {
        ArrayList<Location> circlesBlocks = new ArrayList<>();
        int bX = center.getBlockX();
        int bY = center.getBlockY();
        int bZ = center.getBlockZ();

        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = ((bX - x) * (bX - x) + ((bZ - z) * (bZ - z)) + ((bY - y) * (bY - y)));
                    if (distance < radius * radius) {
                        Location block = new Location(center.getWorld(), x, y, z);
                        circlesBlocks.add(block);
                    }
                }
            }
        }
        return circlesBlocks;
    }


    private boolean checkPermission(CommandSender sender, String perm, String comments) {
        if (sender.hasPermission(perm)) {
            return true;
        }
        new NoPermission().sendMessage(sender instanceof Player ? ((Player) sender).getUniqueId() : null, ((Player) sender).getUniqueId() + ": " + perm + (comments.trim().isEmpty() ? ("") : (" " + comments)), true);
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "lang":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.lang", "")) {
                            if (args.length > 1) {
                                for (Languages language : Languages.getLanguages()) {
                                    if (args[1].equalsIgnoreCase(language.getId()) || args[1].equalsIgnoreCase(language.getName())) {
                                        UHCPlayer lgp = UHCPlayer.thePlayer((Player) sender);
                                        lgp.setLanguage(language);
                                        lgp.saveLang();
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "kill":
                    if (checkPermission(sender, "lg.admin", "")) {
                        UHCPlayer lgp4 = UHCPlayer.thePlayer((Player) sender);
                        NPC npc = new NPC(lgp4.getName(), lgp4.getPlayer().getLocation(), getPlugin());
                        npc.addRecipient(lgp4.getPlayer());
                        EntityPlayer playerNMS = ((CraftPlayer) lgp4.getPlayer()).getHandle();
                        GameProfile profile = playerNMS.getProfile();
                        Property property = profile.getProperties().get("textures").iterator().next();
                        String texture = property.getValue();
                        String signature = property.getSignature();
                        npc.setSkin(texture, signature);
                        npc.spawn(false, true);
                        npc.setSleep(true, npc.getDirectionInversed(lgp4.getPlayer().getLocation()), false);
                        npc.spectateFix(lgp4.getPlayer(), lgp4.getPlayer().getLocation().getYaw(), true);
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                lgp4.getPlayer().setSpectatorTarget(lgp4.getPlayer());
                                npc.spectateFix(lgp4.getPlayer(), lgp4.getPlayer().getLocation().getYaw(), true);
                                if (i == 5 * 20) {
                                    cancel();
                                    npc.spectate(lgp4.getPlayer(), false);
                                }
                                i++;
                            }

                        }.runTaskTimer(getPlugin(), 1, 1);
                    }
                    break;

                case "start":
                    if (checkPermission(sender, "lg.start", "")) {
                        getGameInstance().updateStart();
                    }
                    break;

                case "join":
                        if (checkPermission(sender, "lg.join", "")) {
                            if (args.length > 1) {
                                Player target = Bukkit.getPlayerExact(args[1]);
                                if (target != null) {
                                    if (!getGameInstance().getGamePlayers().contains(UHCPlayer.thePlayer(target))) {
                                        getGameInstance().join(UHCPlayer.thePlayer(target));
                                        target.sendMessage(UHCPlayer.thePlayer(target).getLanguage().getMessage("commandsJoinMessage"));
                                    }
                                }
                            } else {
                                if (sender instanceof Player) {
                                    if (!getGameInstance().getGamePlayers().contains(UHCPlayer.thePlayer((Player) sender))) {
                                        getGameInstance().join(UHCPlayer.thePlayer((Player) sender));
                                        sender.sendMessage(UHCPlayer.thePlayer((Player) sender).getLanguage().getMessage("commandsJoinMessage"));
                                    }
                                }
                            }
                        }
                    break;
                case "leave":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.leave", "")) {
                            if (getGameInstance().getGamePlayers().contains(UHCPlayer.thePlayer((Player) sender))) {
                                getGameInstance().getGamePlayers().remove(UHCPlayer.thePlayer((Player) sender));
                                sender.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.GOLD + " Leaved game");
                            }
                        }
                    }
                    break;

                case "gui":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.gui", "")) {
                            ((Player) sender).openInventory(MainGUI.createGUI((Player) sender));
                        }
                    }
                    break;

                case "stats":
                    if (args.length>1) {
                        if (checkPermission(sender, "lg.stats.manage", "")) {
                            switch (args[1]) {
                                case "place":
                                    if (sender instanceof Player) {
                                        if (sender.hasPermission("lg.stats.place"))
                                            HoloStats.createHoloStats(((Player) sender).getLocation(), 9);
                                        else {
                                            new NoPermission().sendMessage(sender instanceof Player ? ((Player) sender).getUniqueId() : null, ((Player) sender).getUniqueId().toString() + "/ stats place command", true);
                                        }
                                    }
                                    break;

                                case "remove":

                                    break;

                                case "movehere":

                                    break;

                                case "refresh":
                                    HoloStats.updateAll();
                                    break;

                                default:
                                    sender.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.RED + " Command inconnue !");
                                    break;
                            }
                        }
                    } else if (checkPermission(sender, "lg.stats", "")) {
                        sender.sendMessage(ChatColor.BOLD + "STATS:\n");
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                        symbols.setGroupingSeparator(' ');
                        DecimalFormat df = new DecimalFormat("###,###", symbols);
                        //DecimalFormat df = new DecimalFormat("###,###.00", symbols);
                        String pointsS = df.format(UHCPlayer.thePlayer((Player) sender).getStats().getPoints());
                        sender.sendMessage(ChatColor.GRAY + "- Points: " + ChatColor.RED + ChatColor.BOLD + pointsS);
                        sender.sendMessage(ChatColor.GRAY + "- Nombre de game: " + ChatColor.RED + ChatColor.BOLD + UHCPlayer.thePlayer((Player) sender).getStats().getGameNumber());
                        sender.sendMessage(ChatColor.GRAY + "- Nombre de game gagné: " + ChatColor.RED + ChatColor.BOLD + UHCPlayer.thePlayer((Player) sender).getStats().getWinnedGames());
                        sender.sendMessage(ChatColor.GRAY + "- Ratio de win (" + ChatColor.GREEN + "game gagné" + ChatColor.GOLD + "/" + ChatColor.GREEN + "game" + ChatColor.GRAY + ") : " + ChatColor.RED + ChatColor.BOLD + UHCPlayer.thePlayer((Player) sender).getStats().getPercentageOfWin());
                        sender.sendMessage(ChatColor.GRAY + "- Kills: " + ChatColor.RED + ChatColor.BOLD + UHCPlayer.thePlayer((Player) sender).getStats().getKills());
                    }
                    break;

                case "tell":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.admin.tell", "")) {
                            if (args.length > 1) {
                                String message = "";
                                for (int i = 1; i < args.length; i++) {
                                    message = message + " " + args[i];
                                }
                                LoupGarouUHC.broadcastMessage(LoupGarouUHC.getPrefix() + " " + ((Player) sender).getDisplayName() + ChatColor.AQUA + "" + ChatColor.BOLD + " »" + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
                            }
                        }
                    }
                    break;

                case "msg":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.msg", "")) {
                            if (args.length > 2) {
                                Player player = Bukkit.getPlayerExact(args[1]);
                                if (player != null /*&& player!=sender*/) {
                                    String message = "";
                                    for (int i = 2; i < args.length; i++) {
                                        message = message + " " + args[i];
                                    }
                                    String msg = ChatColor.AQUA + "[" + ChatColor.GOLD + "me" + ChatColor.RED + " -> " + ChatColor.GOLD + player.getPlayerListName() + ChatColor.AQUA + "]" + ChatColor.RESET + message;
                                    String msg1 = ChatColor.AQUA + "[" + ChatColor.GOLD + ((Player) sender).getPlayerListName() + ChatColor.RED + " -> " + "me" + ChatColor.AQUA + "]" + ChatColor.RESET + message;
                                    String msg2 = ChatColor.AQUA + "[" + ChatColor.GOLD + ((Player) sender).getPlayerListName() + ChatColor.RED + " -> " + ChatColor.GOLD + player.getPlayerListName() + ChatColor.AQUA + "]" + ChatColor.RESET + message;
                                    player.sendMessage(msg1);
                                    sender.sendMessage(msg);
                                    if (UHCPlayer.thePlayer((Player) sender).isInGame() || UHCPlayer.thePlayer(player).isInGame()) {
                                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                            if (onlinePlayer.hasPermission("lg.admin")) {
                                                onlinePlayer.sendMessage(LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " [Admin] " + msg2);
                                            }
                                            if (UHCPlayer.thePlayer(player).isInGame() && UHCPlayer.thePlayer(onlinePlayer).equals(UHCPlayer.thePlayer(player).getGameData().getGame().getHost())) {
                                                onlinePlayer.sendMessage(LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " [Host] " + msg2);
                                            } else if (UHCPlayer.thePlayer((Player) sender).isInGame() && UHCPlayer.thePlayer(onlinePlayer).equals(UHCPlayer.thePlayer((Player) sender).getGameData().getGame().getHost())) {
                                                onlinePlayer.sendMessage(LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " [Host] " + msg2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;

                case "spectate":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.spectate", "")) {
                            if (!UHCPlayer.thePlayer((Player) sender).isInGame() || (UHCPlayer.thePlayer((Player) sender).isInGame() && UHCPlayer.thePlayer((Player) sender).getGameData().isDead()))
                                if (args.length > 1) {
                                    UHCPlayer tlgp = checkIfInGame(args[1]);
                                    if (tlgp!=null) {
                                        ((Player) sender).teleport(tlgp.getPlayerStats().getLastLocation());
                                        ((Player) sender).setGameMode(GameMode.SPECTATOR);
                                        tlgp.getGameData().getGame().getSpectators().add(UHCPlayer.thePlayer((Player) sender));
                                    }
                                }
                                for (UHCPlayer lgp : getPlayers()) {
                                    if (lgp.isInGame()) {
                                        ((Player) sender).teleport(lgp.getPlayerStats().getLastLocation());
                                        ((Player) sender).setGameMode(GameMode.SPECTATOR);
                                        lgp.getGameData().getGame().getSpectators().add(UHCPlayer.thePlayer((Player) sender));
                                        break;
                                    }
                                }
                        }
                    }
                    break;

                case "manage":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.manage", "")) {
                            UHCPlayer lgp = UHCPlayer.thePlayer((Player) sender);
                            if (args.length > 1) {
                                switch (args[1]) {
                                    case "skip":
                                        if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer() != null) {
                                            lgp.getGameData().getGame().getGameTimer().setDay(lgp.getGameData().getGame().getGameTimer().getDays() + 1);
                                        }
                                        break;

                                    case "revive":
                                        if (args.length > 3) {
                                            LGGame game = null;
                                            Role role = null;
                                            Player target = Bukkit.getPlayerExact(args[2]);
                                            String roleName = args[3];
                                            for (int i = 4; i < args.length; i++) {
                                                roleName += " " + args[i];
                                            }
                                            if (target == null) {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + "This player is not online");
                                                return true;
                                            }

                                            UHCPlayer tlgp = UHCPlayer.thePlayer(target);
                                            if (tlgp.isInGame() && !tlgp.getGameData().isDead())
                                                return true;

                                            if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer() != null) {
                                                game = lgp.getGameData().getGame();
                                            } else {
                                                game = getGameInstance();
                                            }
                                            for (Map.Entry<RoleIdentity, Constructor<? extends Role>> sort : Role.getRoleLinkByStringKey().entrySet()) {
                                                if (sort.getKey().getName(lgp.getLanguage()).equalsIgnoreCase(ChatColor.stripColor(roleName))) {
                                                    try {
                                                        role = sort.getValue().newInstance(game);
                                                    } catch (Exception err) {
                                                        Logger.getGeneralLogger().logInConsole("§4§lUne erreur est survenue lors de la sauvegarde des roles");
                                                        Logger.getGeneralLogger().log(err);
                                                    }
                                                }
                                            }
                                            if (role != null) {
                                                LGPlayerGameData data = new LGPlayerGameData(game);
                                                target.setGameMode(GameMode.SURVIVAL);
                                                data.setRole(role);
                                                tlgp.setGameData(data);
                                                role.join(tlgp, true);
                                                game.scatter(tlgp);

                                                game.getRoles().add(role);
                                                game.getRolesWithDeads().add(role);
                                                game.getGamePlayers().add(tlgp);
                                                game.getGamePlayersWithDeads().add(tlgp);
                                                role.OnDiscoverRole(game, tlgp);
                                                tlgp.setChat(game.getData().getGameChat());
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + "Cannot get role for this name");
                                            }
                                        }
                                        break;

                                    case "scatter":
                                        if (args.length > 2) {
                                            Player player = Bukkit.getPlayerExact(args[2]);
                                            if (player != null) {
                                                UHCPlayer lgT = UHCPlayer.thePlayer(player);
                                                if (lgT.isInGame())
                                                    lgT.getGameData().getGame().scatter(lgT);
                                            }
                                        } else {
                                            if (lgp.isInGame())
                                                lgp.getGameData().getGame().scatter(lgp);
                                        }
                                        break;

                                    case "night":
                                        if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer() != null) {
                                            lgp.getGameData().getGame().getGameTimer().setNight(lgp.getGameData().getGame().getGameTimer().getDays());
                                        }
                                        break;

                                    case "name":
                                        if (args.length > 2) {
                                            String name = "";
                                            for (int i = 0; i < args.length; i++) {
                                                if (i == 2) {
                                                    name += args[i];
                                                }
                                                if (i > 2) {
                                                    name += " " + args[i];
                                                }
                                            }
                                            name = ChatColor.translateAlternateColorCodes('&', name);
                                            if (name.length() <= 25) {
                                                if (lgp.isInGame()) {
                                                    lgp.getGameData().getGame().getData().setDisplayName(name);
                                                } else {
                                                    getGameInstance().getData().setDisplayName(name);
                                                }
                                            } else {
                                                //todo error
                                            }
                                        }
                                        break;

                                    case "host":
                                        if (args.length > 2) {
                                            Player player = Bukkit.getPlayerExact(args[2]);
                                            if (player != null) {
                                                UHCPlayer lgT = UHCPlayer.thePlayer(player);
                                                if (lgT.isInGame()) {
                                                    lgT.getGameData().getGame().getData().setHost(lgT);
                                                } else {
                                                    getGameInstance().getData().setHost(lgT);
                                                }
                                            }
                                            //todo message
                                        } else {
                                            if (lgp.isInGame()) {
                                                lgp.getGameData().getGame().getData().setHost(null);
                                            } else {
                                                getGameInstance().getData().setHost(null);
                                            }
                                        }
                                        break;

                                    case "regen":
                                        reloadGameInstance();
                                        break;

                                    case "stop":
                                        if (lgp.isInGame()) {
                                            lgp.getGameData().getGame().endGame(WinType.NONE, false);
                                        }
                                        break;

                                    default:
                                        sender.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.RED + " Commande inconnue !");
                                        break;
                                }
                            }
                        }
                    }
                    break;



                case "vote":
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        UHCPlayer lgp = UHCPlayer.thePlayer(player);
                        if (lgp.isInGame() && lgp.getGameData().canVote()) {
                            PlayerVote playerVote = VoteManager.getPlayerVotes(lgp.getGameData().getGame());

                            if (playerVote==null) {
                                lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + " Le vote n'est pas activé");
                                return true;
                            }

                            if (args.length == 2) {
                                UHCPlayer tlgp = checkIfInGame(lgp.getGameData().getGame(), args[1]);
                                if (tlgp != null) {
                                    if (tlgp.isInGame() && tlgp.getGameData().getGame().equals(lgp.getGameData().getGame())) {
                                        if (!tlgp.getGameData().isDead()) {

                                            boolean hasVoted = false;
                                            for (Map.Entry<Vote, Integer> entry : playerVote.getVotes().entrySet()) {
                                                Vote vote = entry.getKey();
                                                Integer integer = entry.getValue();
                                                if (vote.isSameVoter(lgp))
                                                    hasVoted = true;
                                            }
                                            if (!hasVoted) {
                                                playerVote.getVotes().put(new Vote(lgp, tlgp), 1);
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_GREEN + " Votre vote a bien été comptabilisé.");
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + " Vous avez dejà voté !");
                                            }
                                        } else {
                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + " Vous ne pouvez pas voter cette personne");
                                        }
                                    } else {
                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + " Vous ne pouvez pas voter cette personne");
                                    }
                                } else {
                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + " Cette personne n'existe pas");
                                }
                            } else if (lgp.getGameData().getRole().getRoleIdentity().equals(Corbeau.getStaticRoleIdentity())) {
                                boolean hasVoted = false;
                                for (Vote vote : playerVote.getVotes().keySet()) {
                                    if (vote.isSameVoter(lgp)) {
                                        hasVoted = true;
                                        break;
                                    }
                                }
                                if (!hasVoted) {
                                    ArrayList<UHCPlayer> targetedPlayers = new ArrayList<>();
                                    for (int i = 1; i < args.length; i++) {
                                        UHCPlayer tlgp = checkIfInGame(lgp.getGameData().getGame(), args[i]);
                                        if (tlgp != null) {
                                            targetedPlayers.add(tlgp);
                                        } else {
                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.RED + " Votre vote ne peut être comptabilisé.");
                                            break;
                                        }
                                    }
                                    for (int i = 0; i < targetedPlayers.size(); i++) {
                                        if (i >= 3) {
                                            break;
                                        }
                                        playerVote.getVotes().put(new Vote(lgp, targetedPlayers.get(i)), playerVote.getVotes().getOrDefault(new Vote(lgp, targetedPlayers.get(i)), 0)+1);
                                    }
                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_GREEN + " Votre vote a bien été comptabilisé.");
                                }
                            } else {
                                lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + " Cette personne n'existe pas");
                            }
                        } else {
                            lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + " Vous ne pouvez pas voter !");
                        }
                    }
                    break;

                case "list":
                    if (sender instanceof Player) {
                        UHCPlayer lgp = UHCPlayer.thePlayer((Player) sender);
                        if (lgp.isInGame()) {
                            if (!lgp.getGameData().getGame().getParameters().isHiddenCompo()) {
                                for (Role role : lgp.getGameData().getGame().getRolesWithDeads()) {
                                    if (lgp.getGameData().getGame().getRoles().contains(role)) {
                                        lgp.sendMessage(role.getName(lgp.getLanguage()));
                                    } else {
                                        lgp.sendMessage(ChatColor.STRIKETHROUGH + role.getName(lgp.getLanguage()));
                                    }
                                }
                            }
                        }
                    }
                    break;


                case "role":
                    if (sender instanceof Player) {
                        UHCPlayer lgp = UHCPlayer.thePlayer((Player) sender);
                        if (args.length > 1) {
                            switch (args[1]) {
                                case "see":
                                    if (lgp.isInGame() && lgp.getGameData().getRole().getRoleIdentity().equals(Voyante.getStaticRoleIdentity()) && lgp.getGameData().getGame().getGameTimer().getDays() > lgp.getGameData().getGame().getParameters().getDayRoleDivulged() -1) {
                                        Voyante voyante = (Voyante) lgp.getGameData().getRole();
                                        if (voyante.hasPower()) {
                                            if (args.length > 2) {
                                                UHCPlayer tlgp = checkIfInGame(lgp.getGameData().getGame(), args[2]);
                                                if (tlgp != null) {
                                                    if (tlgp.isInGame() && tlgp.getGameData().getRole() != null && !tlgp.getGameData().isDead()) {
                                                        if (tlgp.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity())) {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " est " + ((LGFeutre) tlgp.getGameData().getRole()).affichage.getName(lgp.getLanguage()));
                                                        } else {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " est " + tlgp.getGameData().getRole().getName(lgp.getLanguage()));

                                                        }
                                                        voyante.setPower(false);
                                                    } else {
                                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur est invalide");
                                                    }
                                                } else {
                                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                }
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu doit mettre un joueur valide !");
                                            }
                                        } else {
                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu ne peut pas voir les roles pour l'instant");
                                        }
                                    } else if (lgp.isInGame() && lgp.getGameData().getRole().getRoleIdentity().equals(Renard.getStaticRoleIdentity()) && lgp.getGameData().getGame().getGameTimer().getDays() > lgp.getGameData().getGame().getParameters().getDayRoleDivulged()-1) {
                                        Renard renard = (Renard) lgp.getGameData().getRole();
                                        if (renard.hasPower()) {
                                            if (args.length > 2) {
                                                UHCPlayer tlgp = checkIfInGame(lgp.getGameData().getGame(), args[2]);
                                                if (tlgp != null) {
                                                    if (tlgp.isInGame() && tlgp.getGameData().hasRole() && !tlgp.getGameData().isDead() && Maths.distanceIn2D(lgp.getPlayer().getLocation(), tlgp.getPlayer().getLocation()) <= renard.getRadius()) {
                                                        if (tlgp.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity())) {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " est " + (((LGFeutre) tlgp.getGameData().getRole()).affichage.getRoleType() == RoleType.LOUP_GAROU ? ChatColor.DARK_RED + "loup garou" : ChatColor.DARK_BLUE + "non loup Garou"));
                                                        } else {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " est " + (tlgp.getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU ? ChatColor.DARK_RED + "loup garou" : ChatColor.DARK_BLUE + "non loup garou"));
                                                        }
                                                        renard.setUse(renard.getUse() + 1);
                                                        renard.setPower(false);
                                                    } else {
                                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur est invalide");
                                                    }
                                                } else {
                                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                }
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu doit mettre un joueur valide !");
                                            }
                                        } else {
                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu ne peut pas voir les roles pour l'instant");
                                        }
                                    } else if (lgp.isInGame() && lgp.getGameData().getRole().getRoleIdentity().equals(Detective.getStaticRoleIdentity()) && lgp.getGameData().getGame().getGameTimer().getDays() > lgp.getGameData().getGame().getParameters().getDayRoleDivulged() -1) {
                                        Detective renard = (Detective) lgp.getGameData().getRole();
                                        if (renard.hasPower()) {
                                            if (args.length > 3) {
                                                UHCPlayer tlgp = checkIfInGame(lgp.getGameData().getGame(), args[2]);
                                                UHCPlayer tlgp2 = checkIfInGame(lgp.getGameData().getGame(), args[3]);
                                                if (tlgp != null && tlgp2!=null) {
                                                    if (tlgp.isInGame() && tlgp.getGameData().hasRole() && !tlgp.getGameData().isDead() && tlgp2.isInGame() && tlgp2.getGameData().hasRole() && !tlgp2.getGameData().isDead()) {
                                                        Role role1 = tlgp.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity()) ? ((LGFeutre) tlgp.getGameData().getRole()).affichage : tlgp.getGameData().getRole();
                                                        Role role2 = tlgp2.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity()) ? ((LGFeutre) tlgp2.getGameData().getRole()).affichage : tlgp2.getGameData().getRole();
                                                        if (role1.getRoleIdentity().getRoleType()==role2.getRoleIdentity().getRoleType()) {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " et " + tlgp2.getName() + " sont dans le même camp");
                                                        } else {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " et " + tlgp2.getName() + " ne sont pas dans le même camp");
                                                        }
                                                        renard.setPower(false);
                                                    } else {
                                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur est invalide");
                                                    }
                                                } else {
                                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                }
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu doit mettre deux joueurs valide !");
                                            }
                                        } else {
                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu ne peut pas voir les roles pour l'instant");
                                        }
                                    } else if (lgp.isInGame() && lgp.getGameData().getRole().getRoleIdentity().equals(Citoyen.getStaticRoleIdentity()) && lgp.getGameData().getGame().getGameTimer().getDays() > lgp.getGameData().getGame().getParameters().getDayRoleDivulged() -1) {
                                        Citoyen citoyen = (Citoyen) lgp.getGameData().getRole();
                                        if (citoyen.isCanCancel()) {
                                            if (args.length > 2 && args[2].equalsIgnoreCase("cancel")) {
                                                if (citoyen.hasPower()) {
                                                    citoyen.setPower(false);
                                                    citoyen.cancelVote();
                                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.GOLD + " Successfully cancelled vote");
                                                } else {
                                                    // TODO: 07/11/20 message
                                                }
                                            } else if (citoyen.getUse() != 0) {
                                                citoyen.setUse(citoyen.getUse() - 1);
                                                String message=ChatColor.GOLD + "" + ChatColor.BOLD + "Votes du jour:\n";
                                                Vote[] votes = VoteManager.convertMapToArray(VoteManager.getPlayerVotes(citoyen.getGame()).getVotes());
                                                for (Vote vote : votes) {
                                                    message+="\n" + ChatColor.AQUA + vote.getVoter().getName() + ChatColor.GOLD + " -> " + ChatColor.AQUA + vote.getVoted().getName();
                                                }
                                                lgp.sendMessage(message);
                                                TextComponent tc = new TextComponent();
                                                if (citoyen.hasPower()) {
                                                    tc.setText(ChatColor.GREEN + "-> " + ChatColor.DARK_GREEN + ChatColor.BOLD + "CANCEL VOTE");
                                                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg role see cancel"));
                                                    tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "Click to cancel votes").create()));
                                                } else {
                                                    tc.setText(ChatColor.RED + "-> " + ChatColor.GRAY + ChatColor.BOLD + "CANCEL VOTE");
                                                    tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "You already cancelled votes").create()));
                                                }

                                                lgp.getPlayer().spigot().sendMessage(tc);
                                            } else {
                                                // TODO: 07/11/20 message
                                            }
                                        } else {
                                            //todo message
                                        }
                                    } else {
                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'est pas voyante ni renard");
                                    }
                                    break;

                                case "respawn":
                                    if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer().getDays() > lgp.getGameData().getGame().getParameters().getDayRoleDivulged() - 1) {
                                        if (lgp.getGameData().getRole().getRoleIdentity().equals(Sorciere.getStaticRoleIdentity())) {
                                            Sorciere sorciere = (Sorciere) lgp.getGameData().getRole();
                                            if (sorciere.hasPower()) {
                                                if (args.length > 2) {
                                                    Player target = Bukkit.getServer().getPlayerExact(args[2]);
                                                    if (target != null) {
                                                        UHCPlayer tlgp = UHCPlayer.thePlayer(target);
                                                        if (tlgp.isInGame() && tlgp.getGameData().getRole() != null && tlgp.getGameData().canBeRespawned()) {
                                                            tlgp.getGameData().setCanBeRespawned(false);
                                                            tlgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.GOLD + " Tu a été ressussié");
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " a été réssuscité !");
                                                            sorciere.setPower(false);
                                                            return true;
                                                        } else {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur est invalide");
                                                        }
                                                    } else {
                                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                    }
                                                } else {
                                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu doit mettre un joueur valide !");
                                                }
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu a déjà ressucité quelqu'un");
                                            }
                                        } else if (lgp.getGameData().getRole().getRoleIdentity().equals(LGInfect.getStaticRoleIdentity())) {
                                            LGInfect infecte = (LGInfect) lgp.getGameData().getRole();
                                            if (infecte.hasPower()) {
                                                if (args.length > 2) {
                                                    UHCPlayer tlgp = checkIfInGame(args[2]);
                                                    if (tlgp != null) {
                                                        if (tlgp.isInGame() && tlgp.getGameData().getRole() != null && tlgp.getGameData().canBeRespawned() && tlgp.getGameData().getRole().getRoleType() != RoleType.LOUP_GAROU && tlgp.getGameData().getKiller()!=null && (tlgp.getGameData().getKiller().getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU || tlgp.getGameData().getKiller().getGameData().getRole().isInfected())) {
                                                            tlgp.getGameData().setCanBeRespawned(false);
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " a été infecté !");
                                                            tlgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.GOLD + " Tu a été infecté, tu gagne maintenant avec les loups garous");
                                                            tlgp.getGameData().setKiller(null);
                                                            tlgp.getGameData().getRole().setInfected(true);
                                                            infecte.setPower(false);
                                                            return true;
                                                        } else {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur est invalide");
                                                        }
                                                    } else {
                                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                    }
                                                } else {
                                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu doit mettre un joueur valide !");
                                                }
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu a déjà ressucité quelqu'un");
                                            }
                                        } else {
                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'est pas sorciere ni infecte");
                                        }
                                    } else {
                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'est pas sorciere ni infecte");
                                    }
                                    break;

                                case "lg":
                                    if (lgp.isInGame() && (lgp.getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU || lgp.getGameData().getRole().isInfected()) && lgp.getGameData().getGame().getGameTimer().getDays() > lgp.getGameData().getGame().getParameters().getDayRoleDivulged()-1) {
                                        String message = LoupGarouUHC.getPrefix() + ChatColor.RED + " ";
                                        boolean empty = true;
                                        for (UHCPlayer player : lgp.getGameData().getGame().getGamePlayersWithDeads()) {
                                            if (player.getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU || lgp.getGameData().getRole().isInfected()) {
                                                if (empty) {
                                                    if (player.getGameData().isDead()) {
                                                        message = message + ChatColor.STRIKETHROUGH + player.getName() + ChatColor.RED;
                                                    } else {
                                                        message = message + player.getName();
                                                    }
                                                } else {
                                                    if (player.getGameData().isDead()) {
                                                        message = message + ", " + ChatColor.STRIKETHROUGH + player.getName() + ChatColor.RED;
                                                    } else
                                                        message = message + ", " + player.getName();
                                                }
                                                empty=false;
                                            }

                                        }
                                        if (!empty) {
                                            lgp.sendMessage(message);
                                        }
                                    } else {
                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'est pas loup garou");
                                    }
                                    break;

                                case "couple":
                                    if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer().getDays() > lgp.getGameData().getGame().getParameters().getDayRoleDivulged()-1) {
                                        if (lgp.getGameData().getRole().getRoleIdentity().equals(Cupidon.getStaticRoleIdentity())) {
                                            Cupidon cupidon = (Cupidon) lgp.getGameData().getRole();
                                            if (cupidon.hasPower()) {
                                                if (args.length > 3) {
                                                    UHCPlayer target1 = checkIfInGame(lgp.getGameData().getGame(), args[2]);
                                                    UHCPlayer target2 = checkIfInGame(lgp.getGameData().getGame(), args[3]);
                                                    if (target1 != target2) {
                                                        if (target1 != null && target2 != null) {
                                                            target1.getGameData().setCouple(target2);
                                                            target2.getGameData().setCouple(target1);
                                                            target1.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " Tu es maintenant uni avec " + ChatColor.DARK_AQUA + target2.getName() + ChatColor.BLUE + ".\nSi l'un de vous meurt, l'autre ne pourras supporter cette souffrance et se suicidera immédiatement.");
                                                            target2.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " Tu es maintenant uni avec " + ChatColor.DARK_AQUA + target1.getName() + ChatColor.BLUE + ".\nSi l'un de vous meurt, l'autre ne pourras supporter cette souffrance et se suicidera immédiatement.");
                                                            cupidon.setPower(false);
                                                        } else {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ces joueurs sont invalide");
                                                        }
                                                    } else {
                                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Ces joueurs sont invalide");
                                                    }
                                                } else {
                                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu doit mettre le pseudo de deux joueurs");
                                                }
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu ne peut pas faire de couple");
                                            }
                                        } else {
                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'est pas cupidon");
                                        }
                                    } else {
                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'est pas cupidon");
                                    }
                                    break;

                                case "don":
                                    if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer() != null) {
                                        if (lgp.getGameData().isInCouple() && lgp.getGameData().getCouple().getPlayer() != null) {
                                            int life = 0;
                                            try {
                                                life = Integer.parseInt(args[2]);
                                            } catch (NumberFormatException e) {
                                                //error
                                            }
                                            int damage = (int) (((float) life / 100) * 20);

                                            if (lgp.getPlayer().getHealth() - damage > 0) {
                                                if (lgp.getGameData().getCouple().getPlayer().getHealth() + damage <= lgp.getGameData().getCouple().getPlayer().getMaxHealth() && lgp.getGameData().getCouple().getPlayer().getHealth() != lgp.getGameData().getCouple().getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                                                    if (life != 0) {
                                                        lgp.getPlayerStats().refresh();
                                                        lgp.getPlayerStats().setHealth(lgp.getPlayer().getHealth() - damage);
                                                        lgp.getPlayerStats().update();
                                                        lgp.getGameData().getCouple().getPlayerStats().refresh();
                                                        lgp.getGameData().getCouple().getPlayerStats().setHealth(lgp.getGameData().getCouple().getPlayer().getHealth() + damage);
                                                        lgp.getGameData().getCouple().getPlayerStats().update();
                                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.GREEN + "Tu a donné " + ChatColor.DARK_GREEN + life + ChatColor.GREEN + "% de vie a " + ChatColor.AQUA + lgp.getGameData().getCouple().getName());
                                                        lgp.getGameData().getCouple().sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.DARK_GREEN + lgp.getName() + ChatColor.GREEN + " vous a donné " + ChatColor.DARK_GREEN + life + ChatColor.GREEN + "% de vie");
                                                    } else {
                                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Impossible de donner 0% de vie a " + ChatColor.AQUA + lgp.getGameData().getCouple().getName());
                                                    }
                                                } else {
                                                    lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + lgp.getGameData().getCouple().getName() + " ne peut recevoir tant de vie");
                                                }
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'a pas assez de vie");
                                            }
                                        } else {
                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'est pas en couple");
                                        }
                                    }
                                    break;

                                default:
                                    sender.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.RED + " Command inconnue !");
                                    break;
                            }
                        } else {
                            if (lgp.isInGame() && lgp.getGameData().hasRole() && lgp.getGameData().getGame().getGameTimer().getDays() > 1) {
                                lgp.getGameData().getRole().displayRole(lgp);
                            }
                        }
                    }
                    break;

                default:
                    sender.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.RED + " Command inconnue !");
                    break;
            }
        } else {
            sender.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.RED + " Cette commande requierd argument !");
        }
        return true;
    }

    private UHCPlayer checkIfInGame(LGGame game, String name) {
        for (UHCPlayer gamePlayer : game.getGamePlayers()) {
            if (gamePlayer.getName().equals(name)) {
                return gamePlayer;
            }
        }
        return null;
    }

    private UHCPlayer checkIfInGame(String name) {
        if (Bukkit.getOfflinePlayer(name).hasPlayedBefore()) {
            UHCPlayer player = UHCPlayer.thePlayer(Bukkit.getOfflinePlayer(name).getUniqueId());
            if (player.isInGame())
                return player;
        }
        return null;
    }

}
