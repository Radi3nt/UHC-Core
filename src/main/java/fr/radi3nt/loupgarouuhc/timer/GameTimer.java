package fr.radi3nt.loupgarouuhc.timer;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.npc.NMSBase;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;
import fr.radi3nt.loupgarouuhc.classes.roles.WinType;
import fr.radi3nt.loupgarouuhc.events.OnDay;
import fr.radi3nt.loupgarouuhc.events.OnDiscoverRole;
import fr.radi3nt.loupgarouuhc.events.OnNewEpisode;
import fr.radi3nt.loupgarouuhc.events.OnNight;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.*;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;

public class GameTimer extends BukkitRunnable {

    private LGGame game;



    private int shift = 1000;

    private int ticks = 0;
    private int ticksday = 0;
    private boolean degas = false;
    private boolean pvp = false;
    private boolean waiting = true;

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

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public GameTimer(LGGame game) {
        this.game = game;
    }


    @Override
    @SuppressWarnings("deprecated")
    public void run() {

        if (waiting) {
            game.getGameSpawn().getWorld().setTime(24000 - shift);
            game.getGameSpawn().getWorld().setFullTime(24000 - shift);
        }

        checkForMessage(ticks);

        if (ticksday>=24000-shift) {
            Bukkit.broadcastMessage(ChatColor.AQUA + "--------- " + langWarperInstance.TIMER_FIN_EP + " " + checkDay(ticks-1) + " ---------");
            Bukkit.getPluginManager().callEvent(new OnNewEpisode(game, checkDay(ticks-1)));
            game.getGameSpawn().getWorld().setTime(24000-shift);
            ticksday=-shift;
        }


        for (LGPlayer lgp : game.getGamePlayersWithDeads()) {
            if (lgp.getCouple()!=null && lgp.isDead() && !lgp.getCouple().isDead()) {
                lgp.getCouple().getPlayer().damage(20);
                lgp.getCouple().setCouple(null);
                break;
            }
            if (lgp.getCouple()!=null && !lgp.isDead() && lgp.getCouple().isDead()) {
                lgp.getPlayer().damage(20);
                lgp.setCouple(null);
            }
        }
        for (LGPlayer lgp : game.getGamePlayers()) {
            if (lgp.getPlayer() != null) {

                lgp.getPlayer().setCompassTarget(game.getGameSpawn());

                if (lgp.getCouple()!=null) {
                    LGPlayer cupi = null;
                    for (LGPlayer lgp1 : game.getGamePlayers()) {
                        if (lgp1.getRole().getRoleSort()==RoleSort.CUPIDON) {
                            cupi=lgp1;
                            break;
                        }
                    }
                    if (cupi!=null && (lgp.getCouple()!=cupi || lgp!=cupi)) {
                        if (lgp.getCouple().getRole().getRoleType() != lgp.getRole().getRoleType() || cupi.getRole().getRoleType()!=lgp.getCouple().getRole().getRoleType() || cupi.getRole().getRoleType()!=lgp.getRole().getRoleType()) {
                            lgp.getRole().setWinType(WinType.COUPLE);
                            lgp.getCouple().getRole().setWinType(WinType.COUPLE);
                        } else {
                            lgp.getRole().setWinType(lgp.getCouple().getRole().getWinType());
                        }
                    } else {
                        if (lgp.getCouple().getRole().getRoleType() != lgp.getRole().getRoleType()) {
                            lgp.getRole().setWinType(WinType.COUPLE);
                            lgp.getCouple().getRole().setWinType(WinType.COUPLE);
                        } else {
                            lgp.getRole().setWinType(lgp.getCouple().getRole().getWinType());
                        }
                    }
                }

                int distance = (int) getDistanceBetween2Points(lgp.getPlayer().getLocation(), game.getGameSpawn());

                String disM = "";
                if (distance <= 100) {
                    disM = "Entre 0 et 100 block du centre";
                }
                if (distance >= 100 && distance < 250) {
                    disM = "Entre 100 et 250 block du centre";
                }
                if (distance >= 250 && distance < 500) {
                    disM = "Entre 250 et 500 block du centre";
                }
                if (distance >= 500 && distance < 750) {
                    disM = "Entre 500 et 750 block du centre";
                }
                if (distance >= 750 && distance < 1000) {
                    disM = "Entre 750 et 1000 block du centre";
                }
                if (distance >= 1000 && distance < 1250) {
                    disM = "Entre 1000 et 1250 block du centre";
                }
                if (distance >= 1250 && distance < 1500) {
                    disM = "Entre 1250 et 1500 block du centre";
                }
                if (distance >= 1500 && distance < 1750) {
                    disM = "Entre 1500 et 1500 block du centre";
                }
                if (distance >= 1750 && distance < 2000) {
                    disM = "Entre 1750 et 2000 block du centre";
                }
                if (distance >= 2000) {
                    disM = "Plus de 2000 block du centre";
                }
                String message = ChatColor.LIGHT_PURPLE + "Distance du centre: " + ChatColor.AQUA + disM;
                lgp.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));


                if (isDivibleBy20(ticks)) {
                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    final Scoreboard board = manager.getNewScoreboard();
                    final Objective objective = board.registerNewObjective("LG UHC", "uhc");

                    //TODO kills, border amelioration
                    int i = 30;
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', game.getData().getDisplayName()));
                    setScore(ChatColor.DARK_GRAY + "    ", i, objective);
                    i--;
                    setScore(ChatColor.AQUA + "Episode " + checkDay(ticks), i, objective);
                    i--;
                    setScore(ChatColor.GOLD + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                            ", i, objective);
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

                    setScore(ChatColor.BLACK + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                            ", i, objective);
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

                    if (!pvp) {
                        int ticksPvp = game.getParameters().getPvpActivate() - ticks;
                        int heures1 = (ticksPvp / 20 / 3600);
                        int minutes1 = ((ticksPvp / 20 - (ticksPvp / 20 / 3600) * 3600) / 60);
                        int seconds1 = ticksPvp / 20 - (heures1 * 3600 + minutes1 * 60);

                        setScore(ChatColor.GOLD + "Pvp: " + ChatColor.YELLOW + String.format("%02d", heures1) + ":" + String.format("%02d", minutes1) + ":" + String.format("%02d", seconds1), i, objective);
                    } else {
                        setScore(ChatColor.GOLD + "Pvp: " + ChatColor.GREEN + "Activated", i, objective);
                    }
                    i--;

                    setScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                            ", i, objective);
                    i--;

                    if (checkDay(ticks)>=game.getParameters().getDayRoleDivulged())
                        setScore(ChatColor.DARK_BLUE + "Role: " + ChatColor.BLUE +lgp.getRole().getName(), i, objective);
                    else
                        setScore(ChatColor.DARK_BLUE + "Role: " + ChatColor.BLUE +ChatColor.MAGIC + String.valueOf(Math.pow(10, new SecureRandom().nextInt(10)+7)), i, objective);
                    i--;


                    setScore(ChatColor.DARK_BLUE + "Kills: " + ChatColor.BLUE + lgp.getKills(), i, objective);
                    i--;

                    setScore(ChatColor.AQUA + "" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                            ", i, objective);
                    i--;

                    String text;
                    if (game.getRadius().equals(game.getParameters().getBaseRadius()))
                        text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + ChatColor.UNDERLINE + "+" + ChatColor.GREEN + game.getRadius();
                    else
                        text = ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + game.getRadius();

                    setScore(text, i, objective);
                    i--;
                    /*
                    setScore("   ", i, objective);
                    i--;
                    setScore(ChatColor.DARK_GREEN + "by Radi3nt", i, objective);

                     */
                    lgp.getPlayer().setScoreboard(board);




                    //TABLIST

                    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();


                    try {
                        Field a = packet.getClass().getDeclaredField("a");
                        a.setAccessible(true);
                        Field b = packet.getClass().getDeclaredField("b");
                        b.setAccessible(true);

                        Object header = new ChatComponentText(ChatColor.RED + "" + ChatColor.BOLD + "Loup Garou UHC\n");//todo custom text
                        Object footer = new ChatComponentText(ChatColor.GOLD + "Dev by " + ChatColor.AQUA + "" + ChatColor.BOLD + "Radi3nt");

                        a.set(packet, header);
                        b.set(packet, footer);

                        NMSBase.sendPacket(packet);


                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }




                if (lgp.getRole().getRoleSort()== RoleSort.LOUP_PERFIDE) {
                    for (LGPlayer tlgp : game.getGamePlayers()) {
                        if (tlgp.getRole().getRoleSort()== RoleSort.PETITE_FILLE) {
                            if (ticksday > 12500 && checkDay(ticks)>1) {
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

    private void checkForMessage(int ticks) {

        if (ticksday==12750 && checkDay(ticks)>1) {
            Bukkit.getPluginManager().callEvent(new OnNight(game));
        }
        if (ticksday==-shift && checkDay(ticks)>1) {
            Bukkit.getPluginManager().callEvent(new OnDay(game));
        }

        if (ticks==3*20) {
            for (LGPlayer lgp : game.getGamePlayersWithDeads()) {
                try {
                    lgp.getPlayer().setWalkSpeed(0.2F);
                    lgp.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                    lgp.getPlayer().removePotionEffect(PotionEffectType.SLOW);
                    lgp.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
                    lgp.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
                    lgp.getPlayer().setGameMode(GameMode.SURVIVAL);
                } catch (NullPointerException e) {}
            }
            if (waiting) {
                game.getGameSpawn().getWorld().setTime(24000 - shift);
                game.getGameSpawn().getWorld().setFullTime(24000 - shift);
                waiting = false;
                ticksday = -shift;
                ticks = 0;
                this.ticks = 0;
            }
        }

        if (ticks==60*20) {
            degas=true;
            Bukkit.broadcastMessage(prefix + ChatColor.DARK_AQUA + " " + langWarperInstance.TIMER_DEGA);
        }

        if (ticks==game.getParameters().getPvpActivate()-(10*60*20)) {
            Bukkit.broadcastMessage(prefix + ChatColor.DARK_AQUA + " " + langWarperInstance.TIMER_PVP_SOON);
        }

        if (ticks==24000*(game.getParameters().getDayRoleDivulged())-23999) {
            for (LGPlayer lgp : game.getGamePlayers()) {
                Role role = lgp.getRole();
                role.join(lgp, true);
            }
            Bukkit.getPluginManager().callEvent(new OnDiscoverRole(game));
        }

        if (game.getParameters().isFinalHeal()) {
            if (ticks==game.getParameters().getFinalHealTime()) {
                for (LGPlayer lgPlayer : game.getGamePlayers()) {
                    if (lgPlayer!=null) {
                        lgPlayer.getPlayer().setHealth(lgPlayer.getPlayer().getMaxHealth());
                    }
                }
                Bukkit.broadcastMessage(prefix + " " + ChatColor.GOLD + "Final heal >" + ChatColor.AQUA + " All players have been healed");
            }
        }

        if (ticks>=game.getParameters().getPvpActivate() && !pvp) {
            pvp = true;
            Bukkit.broadcastMessage(prefix + ChatColor.DARK_AQUA + " " + langWarperInstance.TIMER_PVP);
        }

        if (ticks==24000*(checkDay(ticks))-23999 && checkDay(ticks)>game.getParameters().getMinDayForVote()) { // debut
            if (game.getGamePlayers().size()>=game.getParameters().getMinPlayerForVotes()) {
                Bukkit.broadcastMessage(prefix + " " + ChatColor.RED + "Il reste moins de " + game.getParameters().getMinPlayerForVotes() + " joueurs en vie, le vote est désomais désactivé.");
            } else {
                for (LGPlayer lgp : game.getGamePlayers()) {
                    if (lgp != null) {
                        lgp.setCanVote(true);
                        int heures = (game.getParameters().getGetTimeForVote() / 20 / 3600);
                        int minutes = ((game.getParameters().getGetTimeForVote() / 20 - (game.getParameters().getGetTimeForVote() / 20 / 3600) * 3600) / 60);
                        int seconds = game.getParameters().getGetTimeForVote() / 20 - (heures * 3600 + minutes * 60);
                        if (heures == 0) {
                            if (minutes == 0) {
                                if (seconds == 0) {
                                    System.out.println("Erreur de paramatres");
                                } else {
                                    lgp.sendMessage(prefix + ChatColor.GOLD + " Vous avez " + seconds + " sec pour voter : /lg vote <pseudo>.\n Le joueur recueillant la majorité des votes perdera la moitié de sa vie.");
                                }
                            } else {
                                if (seconds == 0) {
                                    lgp.sendMessage(prefix + ChatColor.GOLD + " Vous avez " + minutes + " min pour voter : /lg vote <pseudo>.\n Le joueur recueillant la majorité des votes perdera la moitié de sa vie.");
                                } else {
                                    lgp.sendMessage(prefix + ChatColor.GOLD + " Vous avez " + minutes + " min " + seconds + " sec pour voter : /lg vote <pseudo>.\n Le joueur recueillant la majorité des votes perdera la moitié de sa vie.");
                                }
                            }
                        } else {
                            System.out.println("Erreur de paramatres");
                            minutes = 59;
                            seconds = 59;
                            lgp.sendMessage(prefix + ChatColor.GOLD + " Vous avez " + minutes + " min " + seconds + " sec pour voter : /lg vote <pseudo>.\n Le joueur recueillant la majorité des votes perdera la moitié de sa vie.");
                        }
                    }
                }
            }
        }
        if (ticks-((checkDay(ticks)-1)*24000)==game.getParameters().getGetTimeForVote() && checkDay(ticks)>game.getParameters().getMinDayForVote()) { // 5min
            if (game.getGamePlayers().size() < game.getParameters().getMinPlayerForVotes()) {
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
                        lgp.setCanVote(false);
                    }
                    Bukkit.broadcastMessage(prefix + ChatColor.GOLD + " " + ChatColor.BOLD + "RESULTAT DU VOTE :");
                    Bukkit.broadcastMessage(prefix + ChatColor.AQUA + " Le joueur " + ChatColor.BOLD + result.getName() + ChatColor.AQUA + " est le joueur ayant le plus de votes : " + ChatColor.BLUE + ChatColor.BOLD + max + ChatColor.AQUA + ". Il perd la moitié de sa vie.");
                    result.getPlayer().setMaxHealth(result.getPlayer().getMaxHealth() / 2);
                } else {
                    for (LGPlayer lgp : game.getGamePlayers()) {
                        lgp.setCanVote(false);
                    }
                    Bukkit.broadcastMessage(prefix + ChatColor.GOLD + " " + ChatColor.BOLD + "RESULTAT DU VOTE :");
                    Bukkit.broadcastMessage(prefix + ChatColor.AQUA + " Personne n'est victime du vote.");
                }
            }
            game.getVoted().clear();
        }
    }

    public int checkDay(int ticks) {
        return ticks/(24000/game.getParameters().getTimeMultiplication())+1;
    }

    public void setDay(Integer days) {
        for ( ; ticks < (days-1)*(24000/game.getParameters().getTimeMultiplication())-20; ticks++) {
            checkForMessage(ticks);
        }
        ticksday=(24000-shift)-20;
        ticks=(days-1)*(24000/game.getParameters().getTimeMultiplication())-20;
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

    private boolean isDivibleBy20( int number )
    {
        return number != 0 && (number % 20 == 0);
    }

    /**
     * This method use the pythagoras theorem
     *
     * @param point1 first point
     * @param point2 2nd point
     * @return distance between two point
     */
    private double getDistanceBetween2Points(Location point1, Location point2) {
        double distance;

        double x1 = point1.getX();
        double z1 = point1.getZ();

        double x2 = point2.getX();
        double z2 = point2.getZ();

        if (x1<0) {
            x1=-x1;
        }
        if (z1<0) {
            z1=-z1;
        }

        x1=x1+x2;
        z1=z1+z2;

        double x = (int) x1*x1;
        double z = (int) z1*z1;

        distance=x+z;
        distance=Math.sqrt(distance);

        return distance;
    }

    private void setScore(String text, int i, Objective objective) {
        Score score = objective.getScore(text);
        score.setScore(i);
    }

}
