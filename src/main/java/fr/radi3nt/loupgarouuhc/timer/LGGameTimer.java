package fr.radi3nt.loupgarouuhc.timer;

import fr.radi3nt.uhc.api.chats.Chat;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.npc.NMSBase;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.event.events.*;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.roles.LoupGarou.LoupPerfide;
import fr.radi3nt.loupgarouuhc.roles.roles.Villagers.PetiteFille;
import fr.radi3nt.loupgarouuhc.roles.roles.Villagers.Villager;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioUtilis;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Field;
import java.security.SecureRandom;

public class LGGameTimer extends GameTimer {

    private LGGame game;

    private int shift = 1000;

    private int ticks = 0;
    private int ticksday = 0;
    private boolean degas = false;
    private boolean waiting = true;


    public LGGameTimer(LGGame game) {
        this.game = game;
    }

    private static void addGhost(Player player, Team team) {
        team.setAllowFriendlyFire(true);
        team.setCanSeeFriendlyInvisibles(true);
        team.addPlayer(player);
    }

    @Override
    @SuppressWarnings("deprecated")
    public void run() {
        if (waiting) {
            game.getGameSpawn().getWorld().setTime(24000 - shift);
            game.getGameSpawn().getWorld().setFullTime(24000 - shift);
            ticksday = -shift;
            for (UHCPlayer gamePlayer : game.getGamePlayersWithDeads()) {
                if (ticks == 1) {
                    gamePlayer.sendTitle(ChatColor.GOLD + "3", "", 10, 20, 10);
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, SoundCategory.AMBIENT, 1f, 1.2f);
                }
                if (ticks == 20) {
                    gamePlayer.sendTitle(ChatColor.GOLD + "2", "", 10, 20, 10);
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, SoundCategory.AMBIENT, 1f, 1.1f);
                }
                if (ticks == 2 * 20) {
                    gamePlayer.sendTitle(ChatColor.GOLD + "1", "", 10, 20, 10);
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, SoundCategory.AMBIENT, 1f, 1f);
                }
                if (ticks == 3 * 20) {
                    gamePlayer.sendTitle(ChatColor.GOLD + gamePlayer.getLanguage().getMessage("gameStartTile", gamePlayer), gamePlayer.getLanguage().getMessage("gameStartSubTile", gamePlayer), 20, 20 * 3, 20);
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, SoundCategory.AMBIENT, 1f, 1f);
                }
            }
        }

        checkForMessage(ticks);

        if (ticksday >= 24000 - shift) {
            for (UHCPlayer lgp : game.getGamePlayersWithDeads()) {
                lgp.sendMessage(lgp.getLanguage().getMessage("gameTimerNewEpisode", lgp));
                if (lgp.getPlayer() != null && lgp.getPlayer().isOnline()) {
                    lgp.getPlayer().playSound(lgp.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, SoundCategory.AMBIENT, 1f, 2f);
                }
            }
            if (!game.isRoleTrolled() || ticks > game.getParameters().getTrollEndTime()) {
                Bukkit.getPluginManager().callEvent(new OnNewEpisode(game, checkDay(ticks - 1)));
            }
            game.getGameSpawn().getWorld().setTime(24000 - shift);
            ticksday = -shift;
        }

        for (UHCPlayer lgp : game.getGamePlayersWithDeads()) {
            if (lgp.getGameData().getCouple() != null && lgp.getGameData().isDead() && !lgp.getGameData().getCouple().getGameData().isDead()) {
                lgp.getGameData().getCouple().getPlayer().damage(20);
                break;
            }
            if (lgp.getGameData().getCouple() != null && !lgp.getGameData().isDead() && lgp.getGameData().getCouple().getGameData().isDead()) {
                lgp.getPlayer().damage(20);
            }
            if (isDivibleBy20(ticks)) {
                if (lgp.getPlayer() != null && lgp.getGameData().isDead()) {
                    lgp.getPlayer().setScoreboard(createSpectatorScoreBoard(lgp));
                }
            }
        }

        for (UHCPlayer spectator : game.getSpectators()) {
            if (isDivibleBy20(ticks)) {
                if (spectator.getPlayer() != null) {
                    spectator.getPlayer().setScoreboard(createSpectatorScoreBoard(spectator));
                }
            }
        }

        for (UHCPlayer lgp : game.getGamePlayers()) {
            lgp.getPlayerStats().refresh();

            if (lgp.getPlayer() != null) {
                lgp.getPlayer().setCompassTarget(game.getGameSpawn());


                if (isDivibleBy20(ticks)) {
                    lgp.getPlayer().setScoreboard(createScoreBoard(lgp));
                    try {
                        Object packet = NMSBase.getNMSClass("PacketPlayOutPlayerListHeaderFooter").newInstance();
                        Field a = packet.getClass().getDeclaredField("a");
                        a.setAccessible(true);
                        Field b = packet.getClass().getDeclaredField("b");
                        b.setAccessible(true);
                        Object header = new ChatComponentText(ChatColor.RED + "" + ChatColor.BOLD + "Loup Garou UHC\n");//todo custom text
                        Object footer = new ChatComponentText(ChatColor.GOLD + "Dev by " + ChatColor.AQUA + "" + ChatColor.BOLD + "Radi3nt");
                        a.set(packet, header);
                        b.set(packet, footer);
                        NMSBase.sendPacket(packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (lgp.getGameData().getRole().getRoleIdentity().equals(LoupPerfide.getStaticRoleIdentity())) {
                    for (UHCPlayer tlgp : game.getGamePlayers()) {
                        if (tlgp.getGameData().getRole().getRoleIdentity() == PetiteFille.getStaticRoleIdentity()) {
                            if (ticksday > 12500 && checkDay(ticks) > 1) {
                                addGhost(tlgp.getPlayer(), createScoreBoard(tlgp).registerNewTeam("ghostsPlayers"));
                                addGhost(lgp.getPlayer(), createScoreBoard(lgp).registerNewTeam("ghostsPlayers"));

                                lgp.getPlayer().spawnParticle(Particle.FIREWORKS_SPARK, tlgp.getPlayer().getLocation().add(0, 1.5, 0), 1, 0, 0, 0, 0);
                                tlgp.getPlayer().spawnParticle(Particle.FIREWORKS_SPARK, lgp.getPlayer().getLocation().add(0, 1.5, 0), 1, 0, 0, 0, 0);
                            }
                        }
                    }
                }
            }
        }
        game.getGameSpawn().getWorld().setTime(ticksday);



        ticks++;
        ticksday = ticksday + game.getParameters().getTimeMultiplication();
        //game.updateKill(false); //TODO a remettre

    }

    public int checkDay(int ticks) {
        return ticks / (24000 / game.getParameters().getTimeMultiplication()) + 1;
    }

    public void setNight(Integer days) {
        for (; ticks < ((days - 1) * (24000 / game.getParameters().getTimeMultiplication()) - 20) + (24000 / 2 + shift); ticks++) {
            checkForMessage(ticks);
        }
        ticksday = (24000 / 2 + shift) - 20;
        ticks = ((days - 1) * (24000 / game.getParameters().getTimeMultiplication()) - 20) + (24000 / 2 + shift);
        if (!degas) {
            degas = true;
        }
        if (waiting) {
            for (UHCPlayer lgp : game.getGamePlayersWithDeads()) {
                if (lgp.getPlayer() != null)
                    lgp.getPlayer().setWalkSpeed(0.2F);
                lgp.getPlayerStats().refresh();
                lgp.getPlayerStats().removePotionEffect(PotionEffectType.JUMP);
                lgp.getPlayerStats().removePotionEffect(PotionEffectType.SLOW);
                lgp.getPlayerStats().update();
            }
            waiting = false;
        }
    }

    public void setDay(Integer days) {
        for (; ticks < (days - 1) * (24000 / game.getParameters().getTimeMultiplication()) - 20; ticks++) {
            checkForMessage(ticks);
        }
        ticksday = (24000 - shift) - 20;
        ticks = (days - 1) * (24000 / game.getParameters().getTimeMultiplication()) - 20;
        if (!degas) {
            degas = true;
        }
        if (waiting) {
            for (UHCPlayer lgp : game.getGamePlayersWithDeads()) {
                if (lgp.getPlayer() != null) {
                    lgp.getPlayer().setWalkSpeed(0.2F);
                    lgp.getPlayer().setGameMode(GameMode.SURVIVAL);
                }
                lgp.getPlayerStats().refresh();
                lgp.getPlayerStats().removePotionEffect(PotionEffectType.JUMP);
                lgp.getPlayerStats().removePotionEffect(PotionEffectType.SLOW);
                lgp.getPlayerStats().update();
            }
            waiting = false;
        }
    }

    private boolean isDivibleBy20(int number) {
        return number != 0 && (number % 20 == 0);
    }

    private void checkForMessage(int ticks) {

        int ticksday = ticks - ((checkDay(ticks - shift) - 1) * 24000) - shift;

        if (ticksday == 12000 && checkDay(ticks) > 1) {
            Bukkit.getPluginManager().callEvent(new OnNight(game));
        }
        if (ticksday == 23000 && checkDay(ticks) > 1) {
            if ((game.getParameters().isTroll() || game.isRoleTrolled() || (new SecureRandom().nextBoolean() && game.getParameters().isRandomTroll())) && ticks < game.getParameters().getTrollEndTime()) {
                game.setRoleTrolled(true);
            } else {
                Bukkit.getPluginManager().callEvent(new OnDay(game));
            }
        }

        if (ticks == 3 * 20 && waiting) {
            for (UHCPlayer lgp : game.getGamePlayersWithDeads()) {
                if (lgp.getPlayer() != null) {
                    lgp.getPlayer().setWalkSpeed(0.2F);
                    lgp.getPlayer().setGameMode(GameMode.SURVIVAL);
                }
                lgp.getPlayerStats().refresh();
                lgp.getPlayerStats().removePotionEffect(PotionEffectType.JUMP);
                lgp.getPlayerStats().removePotionEffect(PotionEffectType.SLOW);
                lgp.getPlayerStats().removePotionEffect(PotionEffectType.BLINDNESS);
                lgp.getPlayerStats().removePotionEffect(PotionEffectType.SLOW_DIGGING);
                lgp.getPlayerStats().update();
            }
            this.ticks = 0;
            waiting = false;
        }

        if (ticks==60*20) {
            degas = true;
            Chat.broadcastIdMessage("gameTimerDega", game.getGamePlayersWithDeads());
        }

        if (ticks==24000*(game.getParameters().getDayRoleDivulged())-23999) {
            if (game.isRoleTrolled()) {
                for (UHCPlayer lgp : game.getGamePlayers()) {
                    Role role = new Villager(game);
                    lgp.getGameData().setRole(role);
                    role.displayRole(lgp);
                }
            } else {
                for (UHCPlayer lgp : game.getGamePlayers()) {
                    Role role = lgp.getGameData().getRole();
                    role.join(lgp, true);
                }
                Bukkit.getPluginManager().callEvent(new OnDiscoverRole(game));
            }
        }

        if (ticks == game.getParameters().getTrollEndTime() && game.isRoleTrolled()) {
            game.setRoleTrolled(false);
            Chat.broadcastIdMessage("roleTrollEnd", game.getGamePlayers());
            game.giveRoles(true);
            Bukkit.getPluginManager().callEvent(new OnDiscoverRole(game));
            Bukkit.getPluginManager().callEvent(new OnNewEpisode(game, checkDay(ticks)));
            if (ticksday >= 12000) {
                Bukkit.getPluginManager().callEvent(new OnNight(game));
            } else {
                Bukkit.getPluginManager().callEvent(new OnDay(game));
            }
        }

        ScenarioUtilis.tickAll(this, ticks);
    }

    private void setScore(String text, int i, Objective objective) {
        Score score = objective.getScore(text);
        score.setScore(i);
    }

    private Scoreboard createScoreBoard(UHCPlayer lgp) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("LG UHC", "uhc");

        int i = 30;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', game.getData().getDisplayName()));
        setScore(ChatColor.DARK_GRAY + "    ", i, objective);
        i--;
        setScore(ChatColor.AQUA + "Episode " + checkDay(ticks), i, objective);
        i--;
        setScore(ChatColor.GOLD + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        setScore(ChatColor.RED + "" + game.getGamePlayers().size() + ChatColor.DARK_RED + " joueurs", i, objective);
        i--;

        int nbGroupe = 0;
        if (game.getGamePlayers().size() < 5)
            nbGroupe = 2;
        if (game.getGamePlayers().size() < 10)
            nbGroupe = 3;
        if (game.getGamePlayers().size() >= 10)
            nbGroupe = 4;
        if (game.getGamePlayers().size() >= 15)
            nbGroupe = 5;

        setScore(ChatColor.DARK_RED + "Groupes de " + ChatColor.RED + nbGroupe, i, objective);
        i--;

        setScore(ChatColor.BLACK + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        int heures = (ticks / 20 / 3600);
        int minutes = ((ticks / 20 - (ticks / 20 / 3600) * 3600) / 60);
        int seconds = ticks / 20 - (heures * 3600 + minutes * 60);
        if (waiting) {
            heures = 0;
            minutes = 0;
            seconds = 0;
        }
        setScore(ChatColor.GOLD + "Timer: " + ChatColor.YELLOW + String.format("%02d", heures) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds), i, objective);
        i--;

        if (game.getPvP().isTimerActivated() && !game.getPvP().isPvp()) {
            int ticksPvp = game.getPvP().getTime() - ticks;
            int heures1 = (ticksPvp / 20 / 3600);
            int minutes1 = ((ticksPvp / 20 - (ticksPvp / 20 / 3600) * 3600) / 60);
            int seconds1 = ticksPvp / 20 - (heures1 * 3600 + minutes1 * 60);

            if (waiting) {
                heures1 = 0;
                minutes1 = 0;
                seconds1 = 0;
            }

            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.YELLOW + String.format("%02d", heures1) + ":" + String.format("%02d", minutes1) + ":" + String.format("%02d", seconds1), i, objective);
        } else if (game.getPvP().isPvp()) {
            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.GREEN + "Activated", i, objective);
        } else if (!game.getPvP().isTimerActivated()) {
            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.DARK_RED + "Deactivated", i, objective);
        }
        i--;

        setScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective); //todo tro longue barres
        i--;

        if (checkDay(ticks) >= game.getParameters().getDayRoleDivulged())
            setScore(ChatColor.DARK_BLUE + "Role: " + ChatColor.BLUE + lgp.getGameData().getRole().getName(lgp.getLanguage()), i, objective);
        else
            setScore(ChatColor.DARK_BLUE + "Role: " + ChatColor.BLUE + ChatColor.MAGIC + Math.pow(10, new SecureRandom().nextInt(10) + 7), i, objective);
        i--;


        setScore(ChatColor.DARK_BLUE + "Kills: " + ChatColor.BLUE + lgp.getGameData().getKills(), i, objective);
        i--;

        setScore(ChatColor.AQUA + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        String text;
        if (game.getRadius().equals(game.getParameters().getBaseRadius()))
            text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + ChatColor.UNDERLINE + "+" + ChatColor.GREEN + game.getRadius();
        else
            text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + game.getRadius();

        setScore(text, i, objective);
        i--;

        setScore("   ", i, objective);
        i--;
        setScore(ChatColor.DARK_GREEN + "@Radi3nt", i, objective);
        return board;
    }

    private Scoreboard createSpectatorScoreBoard(UHCPlayer lgp) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("LG UHC", "uhc");

        int i = 30;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', game.getData().getDisplayName()));
        setScore(ChatColor.DARK_GRAY + "    ", i, objective);
        i--;
        setScore(ChatColor.AQUA + "Episode " + checkDay(ticks), i, objective);
        i--;
        setScore(ChatColor.GOLD + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        setScore(ChatColor.RED + "" + game.getGamePlayers().size() + ChatColor.DARK_RED + " joueurs", i, objective);
        i--;

        int nbGroupe = 0;
        if (game.getGamePlayers().size() < 5)
            nbGroupe = 2;
        if (game.getGamePlayers().size() < 10)
            nbGroupe = 3;
        if (game.getGamePlayers().size() >= 10)
            nbGroupe = 4;
        if (game.getGamePlayers().size() >= 15)
            nbGroupe = 5;

        setScore(ChatColor.DARK_RED + "Groupes de " + ChatColor.RED + nbGroupe, i, objective);
        i--;

        setScore(ChatColor.BLACK + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective);
        i--;

        int heures = (ticks / 20 / 3600);
        int minutes = ((ticks / 20 - (ticks / 20 / 3600) * 3600) / 60);
        int seconds = ticks / 20 - (heures * 3600 + minutes * 60);
        if (waiting) {
            heures = 0;
            minutes = 0;
            seconds = 0;
        }
        setScore(ChatColor.GOLD + "Timer: " + ChatColor.YELLOW + String.format("%02d", heures) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds), i, objective);
        i--;

        if (game.getPvP().isTimerActivated() && !game.getPvP().isPvp()) {
            int ticksPvp = game.getPvP().getTime() - ticks;
            int heures1 = (ticksPvp / 20 / 3600);
            int minutes1 = ((ticksPvp / 20 - (ticksPvp / 20 / 3600) * 3600) / 60);
            int seconds1 = ticksPvp / 20 - (heures1 * 3600 + minutes1 * 60);

            if (waiting) {
                heures1 = 0;
                minutes1 = 0;
                seconds1 = 0;
            }

            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.YELLOW + String.format("%02d", heures1) + ":" + String.format("%02d", minutes1) + ":" + String.format("%02d", seconds1), i, objective);
        } else if (game.getPvP().isPvp()) {
            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.GREEN + "Activated", i, objective);
        } else if (!game.getPvP().isTimerActivated()) {
            setScore(ChatColor.GOLD + "Pvp: " + ChatColor.DARK_RED + "Deactivated", i, objective);
        }
        i--;

        setScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                       ", i, objective); //todo tro longue barres
        i--;

        String text;
        if (game.getRadius().equals(game.getParameters().getBaseRadius()))
            text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + ChatColor.UNDERLINE + "+" + ChatColor.GREEN + game.getRadius();
        else
            text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + game.getRadius();

        setScore(text, i, objective);
        i--;

        setScore("   ", i, objective);
        i--;
        setScore(ChatColor.DARK_GREEN + "@Radi3nt", i, objective);
        return board;
    }

    public LGGame getGame() {
        return game;
    }

    public void setGame(LGGame game) {
        this.game = game;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getDays() {
        return checkDay(ticks);
    }

    public int getTicksday() {
        return ticksday;
    }

    public void setTicksday(int ticksday) {
        this.ticksday = ticksday;
    }

    public boolean isDegas() {
        return degas;
    }

    public void setDegas(boolean degas) {
        this.degas = degas;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }


}
