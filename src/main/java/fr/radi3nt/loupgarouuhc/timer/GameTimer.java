package fr.radi3nt.loupgarouuhc.timer;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.chats.Chat;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.npc.NMSBase;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.event.events.OnDay;
import fr.radi3nt.loupgarouuhc.event.events.OnDiscoverRole;
import fr.radi3nt.loupgarouuhc.event.events.OnNewEpisode;
import fr.radi3nt.loupgarouuhc.event.events.OnNight;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou.LoupPerfide;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Solo.Cupidon;
import fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers.PetiteFille;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioUtilis;
import fr.radi3nt.loupgarouuhc.utilis.Maths;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameTimer extends BukkitRunnable {

    private LGGame game;



    private int shift = 1000;

    private int ticks = 0;
    private int ticksday = 0;
    private boolean degas = false;
    private boolean waiting = true;


    public GameTimer(LGGame game) {
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
            for (LGPlayer gamePlayer : game.getGamePlayersWithDeads()) {
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

        for (LGPlayer lgp : game.getGamePlayersWithDeads()) {
            if (ticksday >= 24000 - shift) {
                lgp.sendMessage(lgp.getLanguage().getMessage("gameTimerNewEpisode", lgp));
                if (lgp.getPlayer() != null) {
                    lgp.getPlayer().playSound(lgp.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, SoundCategory.AMBIENT, 1f, 2f);
                }
                Bukkit.getPluginManager().callEvent(new OnNewEpisode(game, checkDay(ticks - 1)));
                game.getGameSpawn().getWorld().setTime(24000 - shift);
                ticksday = -shift;
            }
            if (lgp.getGameData().getCouple() != null && lgp.getGameData().isDead() && !lgp.getGameData().getCouple().getGameData().isDead()) {
                lgp.getGameData().getCouple().getPlayer().damage(20);
                break;
            }
            if (lgp.getGameData().getCouple() != null && !lgp.getGameData().isDead() && lgp.getGameData().getCouple().getGameData().isDead()) {
                lgp.getPlayer().damage(20);
            }
        }

        for (LGPlayer lgp : game.getGamePlayers()) {
            if (lgp.getPlayer() != null) {

                lgp.getPlayer().setCompassTarget(game.getGameSpawn());

                if (lgp.getGameData().isInCouple()) {
                    LGPlayer cupi = null;
                    for (LGPlayer lgp1 : game.getGamePlayers()) {
                        if (lgp1.getGameData().getRole().getRoleIdentity() == Cupidon.getStaticRoleIdentity()) {
                            cupi = lgp1;
                            break;
                        }
                    }
                    if (cupi != null && (lgp.getGameData().getCouple() != cupi || lgp != cupi)) {
                        if (lgp.getGameData().getCouple().getGameData().getRole().getRoleType() != lgp.getGameData().getRole().getRoleType() || cupi.getGameData().getRole().getRoleType() != lgp.getGameData().getCouple().getGameData().getRole().getRoleType() || cupi.getGameData().getRole().getRoleType() != lgp.getGameData().getRole().getRoleType()) {
                            lgp.getGameData().getRole().setWinType(WinType.COUPLE);
                            lgp.getGameData().getCouple().getGameData().getRole().setWinType(WinType.COUPLE);
                        } else {
                            lgp.getGameData().getRole().setWinType(lgp.getGameData().getCouple().getGameData().getRole().getWinType());
                        }
                    } else {
                        if (lgp.getGameData().getCouple().getGameData().getRole().getRoleType() != lgp.getGameData().getRole().getRoleType()) {
                            lgp.getGameData().getRole().setWinType(WinType.COUPLE);
                            lgp.getGameData().getCouple().getGameData().getRole().setWinType(WinType.COUPLE);
                        } else {
                            lgp.getGameData().getRole().setWinType(lgp.getGameData().getCouple().getGameData().getRole().getWinType());
                        }
                    }
                }

                int distance = (int) Maths.getDistanceBetween2Points(lgp.getPlayer().getLocation(), game.getGameSpawn());

                String disM = lgp.getLanguage().getMessage("gameTimerActionBar", lgp);
                String disS = "NaN";
                int maxNumber = 0;
                Integer lastNumber = 0;

                ArrayList<Integer> base = new ArrayList<>();
                base.add(0);
                base.add(100);
                base.add(250);
                base.add(500);
                base.add(750);
                base.add(1000);
                base.add(1250);
                base.add(1500);
                base.add(1750);
                base.add(2000);
                for (Integer integer : base) {
                    if (integer < distance) {
                        lastNumber = integer;
                    } else if (lastNumber != null) {
                        disS = disM.replace("%1%", String.valueOf(lastNumber)).replace("%2%", String.valueOf(integer));
                        lastNumber = null;
                    }
                    if (maxNumber < integer) {
                        maxNumber = integer;
                    }
                }
                if (distance == 0) {
                    //todo distance - message
                }
                if (disS.equals("NaN")) {
                    disS = lgp.getLanguage().getMessage("gameTimerActionBarMax").replace("%1%", String.valueOf(maxNumber));
                }
                lgp.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(disS));


                if (isDivibleBy20(ticks)) {

                    lgp.getPlayer().setScoreboard(createScoreBoard(lgp));

                    //TABLIST

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
                    for (LGPlayer tlgp : game.getGamePlayers()) {
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
                game.getGameSpawn().getWorld().setTime(ticksday);



            }
        }


        //--
        game.getParameters().getWorldBorderTime().put(0, game.getRadius());
        //--

        game.getParameters().getWorldBorderTime().put(2*20*60*20/game.getParameters().getTimeMultiplication(), 1000);
        game.getParameters().getWorldBorderTime().put(6*20*60*20/game.getParameters().getTimeMultiplication(), 300);
        game.getParameters().getWorldBorderTime().put(8*20*60*20/game.getParameters().getTimeMultiplication(), 100);
        game.getParameters().getWorldBorderTime().put(9*20*60*20/game.getParameters().getTimeMultiplication(), 50);
        Integer min = 0;
        for (Map.Entry<Integer, Integer> minKey : game.getParameters().getWorldBorderTime().entrySet()) {
            if (minKey.getKey()<=ticks && minKey.getKey()>min) {
                min=minKey.getKey();
            }
        }
        for (Map.Entry<Integer, Integer> maxKey : game.getParameters().getWorldBorderTime().entrySet()) {
            if (maxKey.getKey()>=ticks && !min.equals(maxKey.getKey())) {
                float coef = ((float) maxKey.getValue()-game.getParameters().getWorldBorderTime().getOrDefault(min, 0))/((float)maxKey.getKey()-min);
                double worldBorderSize = game.getParameters().getWorldBorderTime().getOrDefault(min, 0)+(coef*(ticks-min));
                game.getGameSpawn().getWorld().getWorldBorder().setSize(worldBorderSize*2);
                game.setRadius((int) worldBorderSize);
                break;
            }
        }


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
            for (LGPlayer lgp : game.getGamePlayersWithDeads()) {
                lgp.getPlayer().setWalkSpeed(0.2F);
                lgp.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                lgp.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            }
            waiting = false;
        }
        game.getVoted().clear();
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
            for (LGPlayer lgp : game.getGamePlayersWithDeads()) {
                lgp.getPlayer().setWalkSpeed(0.2F);
                lgp.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                lgp.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            }
            waiting = false;
        }
        game.getVoted().clear();
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
            Bukkit.getPluginManager().callEvent(new OnDay(game));
        }

        if (ticks == 3 * 20 && waiting) {
            for (LGPlayer lgp : game.getGamePlayersWithDeads()) {
                try {
                    lgp.getPlayer().setWalkSpeed(0.2F);
                    lgp.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    lgp.getPlayer().removePotionEffect(PotionEffectType.SLOW);
                    lgp.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
                    lgp.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
                    lgp.getPlayer().setGameMode(GameMode.SURVIVAL);
                } catch (NullPointerException e) {

                }
            }
            this.ticks = 0;
            waiting = false;
        }

        if (ticks==60*20) {
            degas = true;
            Chat.broadcastIdMessage("gameTimerDega", game.getGamePlayersWithDeads());
        }

        if (ticks==24000*(game.getParameters().getDayRoleDivulged())-23999) {
            for (LGPlayer lgp : game.getGamePlayers()) {
                Role role = lgp.getGameData().getRole();
                role.join(lgp, true);
            }
            Bukkit.getPluginManager().callEvent(new OnDiscoverRole(game));
        }

        if (ticks == 24000 * (checkDay(ticks)) - 23999 && checkDay(ticks) >= game.getParameters().getMinDayForVote()) { // debut
            if (game.getGamePlayers().size() <= game.getParameters().getMinPlayerForVotes()) {
                Chat.broadcastIdMessage("gameTimerVoteDeactivated", game.getGamePlayersWithDeads());
            } else {
                for (LGPlayer lgp : game.getGamePlayers()) {
                    if (lgp.isLinkedToPlayer()) {
                        lgp.getGameData().setCanVote(true);
                        int heures = (game.getParameters().getGetTimeForVote() / 20 / 3600);
                        int minutes = ((game.getParameters().getGetTimeForVote() / 20 - (game.getParameters().getGetTimeForVote() / 20 / 3600) * 3600) / 60);
                        int seconds = game.getParameters().getGetTimeForVote() / 20 - (heures * 3600 + minutes * 60);
                        if (heures == 0) {
                            if (minutes == 0) {
                                if (seconds == 0) {
                                    System.out.println("Erreur de paramatres");
                                } else {
                                    lgp.sendMessage(lgp.getLanguage().getMessage("gameTimerVoteMessageSeconds").replace("%voteSeconds%", String.valueOf(seconds)));
                                }
                            } else {
                                if (seconds == 0) {
                                    lgp.sendMessage(lgp.getLanguage().getMessage("gameTimerVoteMessageMinutes").replace("%voteMinutes%", String.valueOf(minutes)));
                                } else {
                                    lgp.sendMessage(lgp.getLanguage().getMessage("gameTimerVoteMessageMinutesSeconds").replace("%voteMinutes%", String.valueOf(minutes)).replace("%voteSeconds%", String.valueOf(seconds)));
                                }
                            }
                        } else {
                            Logger.getLogger().logWhenDebug("Wrong vote time : time is more than 1 hour", LoupGarouUHC.getConsole());
                            minutes = 59;
                            seconds = 59;
                            lgp.sendMessage(lgp.getLanguage().getMessage("gameTimerVoteMessageMinutesSeconds").replace("%voteMinutes%", String.valueOf(minutes)).replace("%voteSeconds%", String.valueOf(seconds)));
                        }
                    }
                }
            }
        }
        if (ticks - ((checkDay(ticks) - 1) * 24000) == game.getParameters().getGetTimeForVote() && checkDay(ticks) >= game.getParameters().getMinDayForVote()) { // 5min
            if (game.getGamePlayers().size() > game.getParameters().getMinPlayerForVotes()) {
                Chat.broadcastIdMessage("gameTimerVoteResultsHeader", game.getGamePlayersWithDeads());
                if (!game.getVoted().isEmpty()) {
                    ArrayList<LGPlayer> votes = new ArrayList<>();
                    game.getVoted().forEach((lgPlayer, lgPlayer2) -> votes.add(lgPlayer2));
                    HashMap<LGPlayer, Integer> hm = new HashMap<>();
                    int value;
                    for (LGPlayer key : votes) {
                        if (hm.containsKey(key)) {
                            value = hm.get(key);
                            hm.put(key, value + 1);
                        } else {
                            hm.put(key, 1);
                        }
                    }
                    int max = 0;
                    LGPlayer result = votes.get(0);
                    for (LGPlayer lgp : game.getGamePlayers()) {
                        if (hm.getOrDefault(lgp, 0) > max) {
                            max = hm.getOrDefault(lgp, 0);
                            result = lgp;
                        }
                        lgp.getPlayer().playSound(lgp.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT, SoundCategory.AMBIENT, 1f, 1f);
                    }
                    for (LGPlayer lgp : game.getGamePlayers()) {
                        lgp.sendMessage(lgp.getLanguage().getMessage("gameTimerVoteResultsPlayer").replace("%votedName%", result.getName()).replace("%numberVotes%", String.valueOf(max)));
                    }
                    result.getPlayer().setMaxHealth(result.getPlayer().getMaxHealth() / 2);
                } else {
                    Chat.broadcastIdMessage("gameTimerVoteResultsNull", game.getGamePlayersWithDeads());
                }
                for (LGPlayer lgp : game.getGamePlayers()) {
                    lgp.getGameData().setCanVote(false);
                }
            }
            game.getVoted().clear();
        }

        ScenarioUtilis.tickAll(this, ticks);
    }

    private void setScore(String text, int i, Objective objective) {
        Score score = objective.getScore(text);
        score.setScore(i);
    }

    private Scoreboard createScoreBoard(LGPlayer lgp) {
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
