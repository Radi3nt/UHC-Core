package fr.radi3nt.loupgarouuhc.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.GUIs.MainGUI;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.lang.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.message.messages.NoPermission;
import fr.radi3nt.loupgarouuhc.classes.npc.NPC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.player.PlayerGameData;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou.LGFeutre;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou.LGInfect;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Solo.Cupidon;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers.Renard;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers.Sorciere;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers.Voyante;
import fr.radi3nt.loupgarouuhc.utilis.Maths;
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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Map;

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

    public void hey() {

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
                                        LGPlayer lgp = LGPlayer.thePlayer((Player) sender);
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
                        LGPlayer lgp4 = LGPlayer.thePlayer((Player) sender);
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
                                    if (!getGameInstance().getGamePlayers().contains(LGPlayer.thePlayer(target))) {
                                        getGameInstance().join(LGPlayer.thePlayer(target));
                                        target.sendMessage(LGPlayer.thePlayer(target).getLanguage().getMessage("commandsJoinMessage"));
                                    }
                                }
                            } else {
                                if (sender instanceof Player) {
                                    if (!getGameInstance().getGamePlayers().contains(LGPlayer.thePlayer((Player) sender))) {
                                        getGameInstance().join(LGPlayer.thePlayer((Player) sender));
                                        sender.sendMessage(LGPlayer.thePlayer((Player) sender).getLanguage().getMessage("commandsJoinMessage"));
                                    }
                                }
                            }
                        }
                    break;
                case "leave":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.leave", "")) {
                            if (getGameInstance().getGamePlayers().contains(LGPlayer.thePlayer((Player) sender))) {
                                getGameInstance().getGamePlayers().remove(LGPlayer.thePlayer((Player) sender));
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
                        String pointsS = df.format(LGPlayer.thePlayer((Player) sender).getStats().getPoints());
                        sender.sendMessage(ChatColor.GRAY + "- Points: " + ChatColor.RED + ChatColor.BOLD + pointsS);
                        sender.sendMessage(ChatColor.GRAY + "- Nombre de game: " + ChatColor.RED + ChatColor.BOLD + LGPlayer.thePlayer((Player) sender).getStats().getGameNumber());
                        sender.sendMessage(ChatColor.GRAY + "- Nombre de game gagné: " + ChatColor.RED + ChatColor.BOLD + LGPlayer.thePlayer((Player) sender).getStats().getWinnedGames());
                        sender.sendMessage(ChatColor.GRAY + "- Ratio de win (" + ChatColor.GREEN + "game gagné" + ChatColor.GOLD + "/" + ChatColor.GREEN + "game" + ChatColor.GRAY + ") : " + ChatColor.RED + ChatColor.BOLD + LGPlayer.thePlayer((Player) sender).getStats().getPercentageOfWin());
                        sender.sendMessage(ChatColor.GRAY + "- Kills: " + ChatColor.RED + ChatColor.BOLD + LGPlayer.thePlayer((Player) sender).getStats().getKills());
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
                                    if (LGPlayer.thePlayer((Player) sender).isInGame() || LGPlayer.thePlayer(player).isInGame()) {
                                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                            if (onlinePlayer.hasPermission("lg.admin")) {
                                                onlinePlayer.sendMessage(LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " [Admin] " + msg2);
                                            }
                                            if (LGPlayer.thePlayer(player).isInGame() && LGPlayer.thePlayer(onlinePlayer).equals(LGPlayer.thePlayer(player).getGameData().getGame().getHost())) {
                                                onlinePlayer.sendMessage(LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " [Host] " + msg2);
                                            } else if (LGPlayer.thePlayer((Player) sender).isInGame() && LGPlayer.thePlayer(onlinePlayer).equals(LGPlayer.thePlayer((Player) sender).getGameData().getGame().getHost())) {
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
                            if (LGPlayer.thePlayer((Player) sender).isInGame() || (LGPlayer.thePlayer((Player) sender).isInGame() && LGPlayer.thePlayer((Player) sender).getGameData().isDead()))
                                for (LGPlayer lgp : getPlayers()) {
                                    if (lgp.isInGame()) {
                                        ((Player) sender).teleport(lgp.getPlayer().getLocation(), PlayerTeleportEvent.TeleportCause.SPECTATE);
                                        ((Player) sender).setGameMode(GameMode.SPECTATOR);
                                        break;
                                    }
                                }
                        }
                    }
                    break;

                case "manage":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.manage", "")) {
                            LGPlayer lgp = LGPlayer.thePlayer((Player) sender);
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

                                            LGPlayer tlgp = LGPlayer.thePlayer(target);
                                            if (tlgp.isInGame() && !tlgp.getGameData().isDead())
                                                return true;

                                            if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer() != null) {
                                                game = lgp.getGameData().getGame();
                                            } else {
                                                game = getGameInstance();
                                            }
                                            for (Map.Entry<RoleIdentity, Constructor<? extends Role>> sort : Role.getRoleLinkByStringKey().entrySet()) {
                                                if (sort.getKey().getName(lgp.getLanguage()).equals(ChatColor.stripColor(roleName))) {
                                                    try {
                                                        role = sort.getValue().newInstance(game);
                                                    } catch (Exception err) {
                                                        Logger.getGeneralLogger().logInConsole("§4§lUne erreur est survenue lors de la sauvegarde des roles");
                                                        Logger.getGeneralLogger().log(err);
                                                    }
                                                }
                                            }
                                            if (role != null) {
                                                game.getRoles().add(role);
                                                game.getRolesWithDeads().add(role);
                                                game.getGamePlayers().add(tlgp);
                                                game.getGamePlayersWithDeads().add(tlgp);
                                                game.scatter(tlgp);
                                                role.join(tlgp, true);
                                                PlayerGameData data = new PlayerGameData(game);
                                                data.setRole(role);
                                                tlgp.setGameData(data);
                                                role.OnDiscoverRole(game, lgp);
                                            } else {
                                                lgp.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_RED + "Cannot get role for this name");
                                            }
                                        }
                                        break;

                                    case "scatter":
                                        if (args.length > 2) {
                                            Player player = Bukkit.getPlayerExact(args[2]);
                                            if (player != null) {
                                                LGPlayer lgT = LGPlayer.thePlayer(player);
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
                                                LGPlayer lgT = LGPlayer.thePlayer(player);
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
                        LGPlayer lgp = LGPlayer.thePlayer(player);
                        if (lgp.isInGame() && lgp.getGameData().canVote()) {
                            if (args.length > 1) {
                                LGPlayer tlgp = checkIfInGame(lgp.getGameData().getGame(), args[1]);
                                if (tlgp != null) {
                                    if (tlgp.isInGame() && tlgp.getGameData().getGame() == lgp.getGameData().getGame()) {
                                        if (!tlgp.getGameData().isDead()) {
                                            if (!lgp.getGameData().getGame().getVoted().containsKey(lgp)) {
                                                lgp.getGameData().getGame().getVoted().put(lgp, tlgp);
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
                        LGPlayer lgp = LGPlayer.thePlayer((Player) sender);
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
                        LGPlayer lgp = LGPlayer.thePlayer((Player) sender);
                        if (args.length > 1) {
                            switch (args[1]) {
                                case "see":
                                    if (lgp.isInGame() && lgp.getGameData().getRole().getRoleIdentity().equals(Voyante.getStaticRoleIdentity()) && lgp.getGameData().getGame().getGameTimer().getDays() > 1) {
                                        Voyante voyante = (Voyante) lgp.getGameData().getRole();
                                        if (voyante.canSee()) {
                                            if (args.length > 2) {
                                                LGPlayer tlgp = checkIfInGame(lgp.getGameData().getGame(), args[2]);
                                                if (tlgp != null) {
                                                    if (tlgp.isInGame() && tlgp.getGameData().getRole() != null && !tlgp.getGameData().isDead()) {
                                                        if (tlgp.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity())) {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " est " + ((LGFeutre) tlgp.getGameData().getRole()).affichage.getName(lgp.getLanguage()));
                                                        } else {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " est " + tlgp.getGameData().getRole().getName(lgp.getLanguage()));

                                                        }
                                                        voyante.setCanSee(false);
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
                                    } else if (lgp.isInGame() && lgp.getGameData().getRole().getRoleIdentity().equals(Renard.getStaticRoleIdentity()) && lgp.getGameData().getGame().getGameTimer().getDays() > 1) {
                                        Renard renard = (Renard) lgp.getGameData().getRole();
                                        if (renard.canSee()) {
                                            if (args.length > 2) {
                                                LGPlayer tlgp = checkIfInGame(lgp.getGameData().getGame(), args[2]);
                                                if (tlgp != null) {
                                                    if (tlgp.isInGame() && tlgp.getGameData().hasRole() && !tlgp.getGameData().isDead() && Maths.getDistanceBetween2Points(lgp.getPlayer().getLocation(), tlgp.getPlayer().getLocation()) <= renard.getRadius()) {
                                                        if (tlgp.getGameData().getRole().getRoleIdentity().equals(LGFeutre.getStaticRoleIdentity())) {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " est " + (((LGFeutre) tlgp.getGameData().getRole()).affichage.getRoleType() == RoleType.LOUP_GAROU ? ChatColor.DARK_RED + "loup garou" : ChatColor.DARK_BLUE + "non loup Garou"));
                                                        } else {
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " est " + (tlgp.getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU ? ChatColor.DARK_RED + "loup garou" : ChatColor.DARK_BLUE + "non loup garou"));
                                                        }
                                                        renard.setTime(renard.getTime() + 1);
                                                        renard.setCanSee(false);
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
                                    } else {
                                        lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + ChatColor.RED + "Tu n'est pas voyante ni renard");
                                    }
                                    break;

                                case "respawn":
                                    if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer().getDays() > lgp.getGameData().getGame().getParameters().getDayRoleDivulged() - 1) {
                                        if (lgp.getGameData().getRole().getRoleIdentity().equals(Sorciere.getStaticRoleIdentity())) {
                                            Sorciere sorciere = (Sorciere) lgp.getGameData().getRole();
                                            if (!sorciere.hasrespwaned) {
                                                if (args.length > 2) {
                                                    Player target = Bukkit.getServer().getPlayerExact(args[2]);
                                                    if (target != null) {
                                                        LGPlayer tlgp = LGPlayer.thePlayer(target);
                                                        if (tlgp.isInGame() && tlgp.getGameData().getRole() != null && tlgp.getGameData().canBeRespawned()) {
                                                            tlgp.getGameData().setCanBeRespawned(false);
                                                            tlgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.GOLD + " Tu a été ressussié");
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " a été réssuscité !");
                                                            sorciere.hasrespwaned = true;
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
                                        } else if (lgp.getGameData().getRole().getRoleIdentity() == LGInfect.getStaticRoleIdentity()) {
                                            LGInfect infecte = (LGInfect) lgp.getGameData().getRole();
                                            if (!infecte.hasrespwaned) {
                                                if (args.length > 2) {
                                                    Player target = Bukkit.getServer().getPlayerExact(args[2]);
                                                    if (target != null) {
                                                        LGPlayer tlgp = LGPlayer.thePlayer(target);
                                                        if (tlgp.isInGame() && tlgp.getGameData().getRole() != null && tlgp.getGameData().canBeRespawned() && tlgp.getGameData().getRoleType() != RoleType.LOUP_GAROU && tlgp.getGameData().getKiller().getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU) {
                                                            tlgp.getGameData().setCanBeRespawned(false);
                                                            lgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " " + tlgp.getName() + " a été infecté !");
                                                            tlgp.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.GOLD + " Tu a été infecté, tu gagne maintenant avec les loups garous");
                                                            tlgp.getGameData().setKiller(null);
                                                            tlgp.getGameData().setInfected(true);
                                                            infecte.hasrespwaned = true;
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
                                    if (lgp.isInGame() && lgp.getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU && lgp.getGameData().getGame().getGameTimer().getDays() > 1) {
                                        String message = LoupGarouUHC.getPrefix() + ChatColor.RED + " ";
                                        boolean empty = true;
                                        for (LGPlayer player : lgp.getGameData().getGame().getGamePlayersWithDeads()) {
                                            if (player.getGameData().getRole().getRoleType() == RoleType.LOUP_GAROU) {
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
                                    if (lgp.isInGame() && lgp.getGameData().getGame().getGameTimer().getDays() > 1) {
                                        if (lgp.getGameData().getRole().getRoleIdentity() == Cupidon.getStaticRoleIdentity()) {
                                            Cupidon cupidon = (Cupidon) lgp.getGameData().getRole();
                                            if (cupidon.canCouple) {
                                                if (args.length > 3) {
                                                    LGPlayer target1 = checkIfInGame(lgp.getGameData().getGame(), args[2]);
                                                    LGPlayer target2 = checkIfInGame(lgp.getGameData().getGame(), args[3]);
                                                    if (target1 != target2) {
                                                        if (target1 != null && target2 != null) {
                                                            target1.getGameData().setCouple(target2);
                                                            target2.getGameData().setCouple(target1);
                                                            target1.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " Tu es maintenant uni avec " + ChatColor.DARK_AQUA + target2.getName() + ChatColor.BLUE + ".\nSi l'un de vous meurt, l'autre ne pourras supporter cette souffrance et se suicidera immédiatement.");
                                                            target2.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " Tu es maintenant uni avec " + ChatColor.DARK_AQUA + target1.getName() + ChatColor.BLUE + ".\nSi l'un de vous meurt, l'autre ne pourras supporter cette souffrance et se suicidera immédiatement.");
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

    private LGPlayer checkIfInGame(LGGame game, String name) {
        for (LGPlayer gamePlayer : game.getGamePlayers()) {
            if (gamePlayer.getName().equals(name)) {
                return gamePlayer;
            }
        }
        return null;
    }

}
