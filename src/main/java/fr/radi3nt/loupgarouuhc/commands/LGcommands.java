package fr.radi3nt.loupgarouuhc.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.radi3nt.loupgarouuhc.classes.GUIs.MainGUI;
import fr.radi3nt.loupgarouuhc.classes.message.m.NoPermission;
import fr.radi3nt.loupgarouuhc.classes.npc.NPC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleType;
import fr.radi3nt.loupgarouuhc.classes.roles.WinType;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou.LGFeutre;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou.LGInfect;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.Solo.Cupidon;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers.Renard;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers.Sorciere;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.Villagers.Voyante;
import fr.radi3nt.loupgarouuhc.classes.stats.HoloStats;
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

import java.util.ArrayList;

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
        new NoPermission().sendMessage((Player) sender, ((Player) sender).getUniqueId() + ": " + perm + (comments.trim().isEmpty() ? ("") : (" " + comments)), true);
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "kill":
                    if (checkPermission(sender, "lg.admin", "")) {
                        LGPlayer lgp4 = LGPlayer.thePlayer((Player) sender);
                        NPC npc = new NPC(lgp4.getName(), lgp4.getPlayer().getLocation(), plugin);
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

                        }.runTaskTimer(plugin, 1, 1);
                    }
                    break;

                case "start":
                    if (checkPermission(sender, "lg.start", "")) {
                        GameInstance.updateStart();
                    }
                    break;

                case "join":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.join", "")) {
                            if (args.length > 1) {
                                Player target = Bukkit.getPlayerExact(args[1]);
                                if (target != null) {
                                    if (!GameInstance.getGamePlayers().contains(LGPlayer.thePlayer(target))) {
                                        GameInstance.join(LGPlayer.thePlayer(target));
                                        target.sendMessage(LGPlayer.thePlayer(target).getLanguage().getMessage("commandsJoinMessage"));
                                    }
                                }
                            } else {
                                if (!GameInstance.getGamePlayers().contains(LGPlayer.thePlayer((Player) sender))) {
                                    GameInstance.join(LGPlayer.thePlayer((Player) sender));
                                    sender.sendMessage(LGPlayer.thePlayer((Player) sender).getLanguage().getMessage("commandsJoinMessage"));
                                }
                            }
                        }
                    }
                    break;
                case "leave":
                    if (sender instanceof Player) {
                        if (checkPermission(sender, "lg.leave", "")) {
                            if (GameInstance.getGamePlayers().contains(LGPlayer.thePlayer((Player) sender))) {
                                GameInstance.getGamePlayers().remove(LGPlayer.thePlayer((Player) sender));
                                sender.sendMessage(prefix + ChatColor.GOLD + " Leaved game");
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
                                            new NoPermission().sendMessage((Player) sender, ((Player) sender).getUniqueId().toString() + "/ stats place command", true);
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
                                    sender.sendMessage(prefix + ChatColor.RED + " Command inconnue !");
                                    break;
                            }
                        }
                    } else if (checkPermission(sender, "lg.stats", "")) {
                        sender.sendMessage(ChatColor.BOLD + "STATS:\n");
                        sender.sendMessage(ChatColor.GRAY + "- Points: " + ChatColor.RED + ChatColor.BOLD + LGPlayer.thePlayer((Player) sender).getStats().getPoints());
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
                                Bukkit.broadcastMessage(prefix + " " + ((Player) sender).getDisplayName() + ChatColor.AQUA + "" + ChatColor.BOLD + " »" + ChatColor.RESET + message);
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
                                    if (LGPlayer.thePlayer((Player) sender).getGame()!=null || LGPlayer.thePlayer(player).getGame()!=null) {
                                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                            if (onlinePlayer.hasPermission("lg.admin")) {
                                                onlinePlayer.sendMessage(prefixPrivé + ChatColor.BLUE + " [Admin] " + msg2);
                                            }
                                            if (LGPlayer.thePlayer(player).getGame()!=null && LGPlayer.thePlayer(onlinePlayer).equals(LGPlayer.thePlayer(player).getGame().getHost())) {
                                                onlinePlayer.sendMessage(prefixPrivé + " [Host] " + msg2);
                                            }
                                            if (LGPlayer.thePlayer((Player) sender).getGame()!=null && LGPlayer.thePlayer(onlinePlayer).equals(LGPlayer.thePlayer((Player) sender).getGame().getHost())) {
                                                onlinePlayer.sendMessage(prefixPrivé + ChatColor.BLUE + " [Host] " + msg2);
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
                            if (LGPlayer.thePlayer((Player) sender).getGame() == null || (LGPlayer.thePlayer((Player) sender).getGame() != null && LGPlayer.thePlayer((Player) sender).isDead()))
                                for (LGPlayer lgp : players) {
                                    if (lgp.getGame() != null) {
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
                                        if (lgp.getGame() != null && lgp.getGame().getGameTimer() != null) {
                                            lgp.getGame().getGameTimer().setDay(lgp.getGame().getGameTimer().getDays() + 1);
                                        }
                                        break;

                                    case "scatter":
                                        if (args.length>2) {
                                            Player player = Bukkit.getPlayerExact(args[2]);
                                            if (player!=null && lgp.getGame()!=null) {
                                                lgp.getGame().scatter(lgp);
                                            }
                                        }
                                        break;

                                    case "night":
                                        if (lgp.getGame() != null && lgp.getGame().getGameTimer() != null) {
                                            lgp.getGame().getGameTimer().setNight(lgp.getGame().getGameTimer().getDays());
                                        }
                                        break;

                                    //todo setTime


                                    default:
                                        sender.sendMessage(prefix + ChatColor.RED + " Command inconnue !");
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
                        if (lgp.canVote() && lgp.getGame() != null) {
                            if (args.length>1) {
                                Player target = Bukkit.getServer().getPlayerExact(args[1]);
                                if (target != null) {
                                    LGPlayer tlgp = LGPlayer.thePlayer(target);
                                    if (tlgp.getGame() != null && tlgp.getGame() == lgp.getGame()) {
                                        if (!tlgp.isDead()) {
                                            if (!lgp.getGame().getVoted().containsKey(lgp)) {
                                                lgp.getGame().getVoted().put(lgp, tlgp);
                                                lgp.sendMessage(prefix + ChatColor.DARK_GREEN + " Votre vote a bien été comptabilisé.");
                                            } else {
                                                lgp.sendMessage(prefix + ChatColor.DARK_RED + " Vous avez dejà voté !");
                                            }
                                        } else {
                                            lgp.sendMessage(prefix + ChatColor.DARK_RED + " Vous ne pouvez pas voter cette personne");
                                        }
                                    } else {
                                        lgp.sendMessage(prefix + ChatColor.DARK_RED + " Vous ne pouvez pas voter cette personne");
                                    }
                                } else {
                                    lgp.sendMessage(prefix + ChatColor.DARK_RED + " Cette personne n'existe pas");
                                }
                            } else {
                                lgp.sendMessage(prefix + ChatColor.DARK_RED + " Cette personne n'existe pas");
                            }
                        } else {
                            lgp.sendMessage(prefix + ChatColor.DARK_RED + " Vous ne pouvez pas voter !");
                        }
                    }
                    break;

                case "list":
                    if (sender instanceof Player) {
                        LGPlayer lgp = LGPlayer.thePlayer((Player) sender);
                        if (lgp.getGame() != null) {
                            if (!lgp.getGame().getCompoCache()) {
                                for (Role role : lgp.getGame().getRolesWithDeads()) {
                                    if (lgp.getGame().getRoles().contains(role)) {
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
                                    if (lgp.getGame() != null && lgp.getRole().getRoleSort() == RoleSort.VOYANTE && lgp.getGame().getGameTimer().getDays() > 1) {
                                        Voyante voyante = (Voyante) lgp.getRole();
                                        if (voyante.canSee()) {
                                            if (args.length > 2) {
                                                Player target = Bukkit.getServer().getPlayerExact(args[2]);
                                                if (target != null) {
                                                    LGPlayer tlgp = LGPlayer.thePlayer(target);
                                                    if (tlgp.getGame() != null && tlgp.getRole() != null && !tlgp.isDead()) {
                                                        if (tlgp.getRole().getRoleSort()==RoleSort.LG_FEUTRE) {
                                                            lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " " + tlgp.getName() + " est " + ((LGFeutre) tlgp.getRole()).affichage.getName(lgp.getLanguage()));
                                                        } else {
                                                            lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " " + tlgp.getName() + " est " + tlgp.getRole().getName(lgp.getLanguage()));

                                                        }
                                                        voyante.setCanSee(false);
                                                    } else {
                                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Ce joueur est invalide");
                                                    }
                                                } else {
                                                    lgp.sendMessage(prefix + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                }
                                            } else {
                                                lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu doit mettre un joueur valide !");
                                            }
                                        } else {
                                            lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu ne peut pas voir les roles pour l'instant");
                                        }
                                    } else if (lgp.getGame() != null && lgp.getRole().getRoleSort() == RoleSort.RENARD && lgp.getGame().getGameTimer().getDays() > 1) {
                                        Renard voyante = (Renard) lgp.getRole();
                                        if (voyante.canSee()) {
                                            if (args.length > 2) {
                                                Player target = Bukkit.getServer().getPlayerExact(args[2]);
                                                if (target != null) {
                                                    LGPlayer tlgp = LGPlayer.thePlayer(target);
                                                    if (tlgp.getGame() != null && tlgp.getRole() != null && !tlgp.isDead() && generateSphere(lgp.getPlayer().getLocation(), voyante.getRadius()).contains(tlgp.getPlayer().getLocation())) {
                                                        if (tlgp.getRole().getRoleSort() == RoleSort.LG_FEUTRE) {
                                                            lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " " + tlgp.getName() + " est " + (((LGFeutre) tlgp.getRole()).affichage.getRoleType() == RoleType.LOUP_GAROU ? ChatColor.DARK_RED + "Loup garou" : ChatColor.YELLOW + "Non loup Garou"));
                                                        } else {
                                                            lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " " + tlgp.getName() + " est " + (tlgp.getRole().getRoleType() == RoleType.LOUP_GAROU ? ChatColor.DARK_RED + "Loup garou" : ChatColor.YELLOW + "Non loup garou"));

                                                        }
                                                        voyante.setCanSee(false);
                                                    } else {
                                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Ce joueur est invalide");
                                                    }
                                                } else {
                                                    lgp.sendMessage(prefix + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                }
                                            } else {
                                                lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu doit mettre un joueur valide !");
                                            }
                                        } else {
                                            lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu ne peut pas voir les roles pour l'instant");
                                        }
                                    } else {
                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu n'est pas voyante ni renard");
                                    }
                                    break;

                                case "respawn":
                                    if (lgp.getGame() != null && lgp.getGame().getGameTimer().getDays() > lgp.getGame().getParameters().getDayRoleDivulged()-1) {
                                        if (lgp.getRole().getRoleSort() == RoleSort.SORCIERE) {
                                            Sorciere sorciere = (Sorciere) lgp.getRole();
                                            if (!sorciere.hasrespwaned) {
                                                if (args.length > 2) {
                                                    Player target = Bukkit.getServer().getPlayerExact(args[2]);
                                                    if (target != null) {
                                                        LGPlayer tlgp = LGPlayer.thePlayer(target);
                                                        if (tlgp.getGame() != null && tlgp.getRole() != null && tlgp.canBeRespawned()) {
                                                            tlgp.setCanBeRespawned(false);
                                                            tlgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.GOLD + " Tu a été ressussié");
                                                            lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " " + tlgp.getName() + " a été réssuscité !");
                                                            sorciere.hasrespwaned = true;
                                                            return true;
                                                        } else {
                                                            lgp.sendMessage(prefix + " " + ChatColor.RED + "Ce joueur est invalide");
                                                        }
                                                    } else {
                                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                    }
                                                } else {
                                                    lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu doit mettre un joueur valide !");
                                                }
                                            } else {
                                                lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu a déjà ressucité quelqu'un");
                                            }
                                        } else if (lgp.getRole().getRoleSort() == RoleSort.LG_INFECTE){
                                            LGInfect infecte = (LGInfect) lgp.getRole();
                                            if (!infecte.hasrespwaned) {
                                                if (args.length > 2) {
                                                    Player target = Bukkit.getServer().getPlayerExact(args[2]);
                                                    if (target != null) {
                                                        LGPlayer tlgp = LGPlayer.thePlayer(target);
                                                        if (tlgp.getGame() != null && tlgp.getRole() != null && tlgp.canBeRespawned() && tlgp.getRoleType()!=RoleType.LOUP_GAROU && tlgp.getKiller().getRole().getRoleType()==RoleType.LOUP_GAROU) {
                                                            tlgp.setCanBeRespawned(false);
                                                            tlgp.getRole().setWinType(WinType.LOUP_GAROU);
                                                            tlgp.getRole().setRoleType(RoleType.LOUP_GAROU);
                                                            lgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " " + tlgp.getName() + " a été infecté !");
                                                            tlgp.sendMessage(prefix + " " + prefixPrivé + ChatColor.GOLD + " Tu a été infecté, tu gagne maintenant avec les loups garous");
                                                            tlgp.setKiller(null);
                                                            infecte.hasrespwaned = true;
                                                            return true;
                                                        } else {
                                                            lgp.sendMessage(prefix + " " + ChatColor.RED + "Ce joueur est invalide");
                                                        }
                                                    } else {
                                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Ce joueur n'existe pas");
                                                    }
                                                } else {
                                                    lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu doit mettre un joueur valide !");
                                                }
                                            } else {
                                                lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu a déjà ressucité quelqu'un");
                                            }
                                        } else {
                                            lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu n'est pas sorciere ni infecte");
                                        }
                                    } else {
                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu n'est pas sorciere ni infecte");
                                    }
                                    break;

                                case "lg":
                                    if (lgp.getGame() != null && lgp.getRole().getRoleType() == RoleType.LOUP_GAROU && lgp.getGame().getGameTimer().getDays() > 1) {
                                        String message = prefix + ChatColor.RED + " ";
                                        boolean empty = true;
                                        for (LGPlayer player : lgp.getGame().getGamePlayersWithDeads()) {
                                            if (player.getRole().getRoleType()==RoleType.LOUP_GAROU) {
                                                if (empty) {
                                                    if (player.isDead()) {
                                                        message=message + ChatColor.STRIKETHROUGH + player.getName() + ChatColor.RED;
                                                    } else {
                                                        message=message + player.getName();
                                                    }
                                                }else {
                                                    if (player.isDead()) {
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
                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu n'est pas loup garou");
                                    }
                                    break;

                                case "couple":
                                    if (lgp.getGame() != null && lgp.getGame().getGameTimer().getDays() > 1) {
                                        if (lgp.getRole().getRoleSort() == RoleSort.CUPIDON) {
                                            Cupidon cupidon = (Cupidon) lgp.getRole();
                                            if (cupidon.canCouple) {
                                                if (args.length>3) {
                                                    Player target1 = Bukkit.getServer().getPlayerExact(args[2]);
                                                    Player target2 = Bukkit.getServer().getPlayerExact(args[3]);
                                                    if (target1!=target2) {
                                                        if (target1 != null && target2 !=null) {
                                                            LGPlayer lgp1 = LGPlayer.thePlayer(target1);
                                                            LGPlayer lgp2 = LGPlayer.thePlayer(target2);


                                                            lgp1.setCouple(lgp2);
                                                            lgp2.setCouple(lgp1);
                                                            lgp1.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " Tu es maintenant uni avec " + ChatColor.DARK_AQUA + lgp2.getName() + ChatColor.BLUE + ".\nSi l'un de vous meurt, l'autre ne pourras supporter cette souffrance et se suicidera immédiatement.");
                                                            lgp2.sendMessage(prefix + " " + prefixPrivé + ChatColor.BLUE + " Tu es maintenant uni avec " + ChatColor.DARK_AQUA + lgp1.getName() + ChatColor.BLUE + ".\nSi l'un de vous meurt, l'autre ne pourras supporter cette souffrance et se suicidera immédiatement.");
                                                        } else {
                                                            lgp.sendMessage(prefix + " " + ChatColor.RED + "Ces joueurs sont invalide");
                                                        }
                                                    } else {
                                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Ces joueurs sont invalide");
                                                    }
                                                } else {
                                                    lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu doit mettre le pseudo de deux joueurs");
                                                }
                                            } else {
                                                lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu ne peut pas faire de couple");
                                            }
                                        } else {
                                            lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu n'est pas cupidon");
                                        }
                                    } else {
                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu n'est pas cupidon");
                                    }
                                    break;

                                case "don":
                                    if (lgp.getGame() != null && lgp.getGame().getGameTimer()!=null) {
                                        if (lgp.getCouple()!=null && lgp.getCouple().getPlayer()!=null) {
                                            int life = 0;
                                            try {
                                                life= Integer.parseInt(args[2]);
                                            } catch (NumberFormatException e) {
                                                //error
                                            }
                                            int damage = (int) (((float) life / 100) * 20);

                                            if (lgp.getPlayer().getHealth()-damage > 0) {
                                                if (lgp.getCouple().getPlayer().getHealth() + damage <= lgp.getCouple().getPlayer().getMaxHealth() && lgp.getCouple().getPlayer().getHealth()!=lgp.getCouple().getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                                                    if (life != 0) {
                                                        lgp.getPlayer().setHealth(lgp.getPlayer().getHealth() - damage);
                                                        lgp.getCouple().getPlayer().setHealth(lgp.getCouple().getPlayer().getHealth() + damage);
                                                        lgp.sendMessage(prefix + " " + ChatColor.GREEN + "Tu a donné " + ChatColor.DARK_GREEN + life + ChatColor.GREEN + "% de vie a " + ChatColor.AQUA + lgp.getCouple().getName());
                                                        lgp.getCouple().sendMessage(prefix + " " + ChatColor.DARK_GREEN + lgp.getName() + ChatColor.GREEN + " vous a donné " + ChatColor.DARK_GREEN + life + ChatColor.GREEN + "% de vie");
                                                    } else {
                                                        lgp.sendMessage(prefix + " " + ChatColor.RED + "Impossible de donner 0% de vie a " + ChatColor.AQUA + lgp.getCouple().getName());
                                                    }
                                                } else {
                                                    lgp.sendMessage(prefix + " " + ChatColor.RED + lgp.getCouple().getName() + " ne peut recevoir tant de vie");
                                                }
                                            } else {
                                                lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu n'a pas assez de vie");
                                            }
                                        } else {
                                            lgp.sendMessage(prefix + " " + ChatColor.RED + "Tu n'est pas en couple");
                                        }
                                    }
                                    break;

                                default:
                                    sender.sendMessage(prefix + ChatColor.RED + " Command inconnue !");
                                    break;
                            }
                        } else {
                            if (lgp.getGame()!=null && lgp.getRole()!=null && lgp.getGame().getGameTimer().getDays()>1) {
                                lgp.getRole().join(lgp, true);
                            }
                        }
                    }
                    break;

                default:
                    sender.sendMessage(prefix + ChatColor.RED + " Command inconnue !");
                    break;
            }
        } else {
            sender.sendMessage(prefix + ChatColor.RED + " Cette commande requierd argument !");
        }
        return true;
    }

}
