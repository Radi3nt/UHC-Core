package fr.radi3nt.uhc.api.scenarios.scenario.vote;

import fr.radi3nt.uhc.api.chats.Chat;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioCommand;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static fr.radi3nt.uhc.api.scenarios.scenario.vote.PlayerVoteUtil.*;

public class PlayerVote extends Scenario {

    private final HashMap<Vote, Integer> votes = new HashMap<>();
    private boolean playerCanVotes = false;

    private int minGapToVote = 3;
    private int timeBetweenVotes = 20 * 60;
    private int timeBeforeAnnounce = 10;
    private int minPlayerToVote = 3;
    private int timeToVote = 60;

    public PlayerVote(UHCGame game) {
        super(game);
    }


    public static String getName() {
        return "Vote";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.SIGN);
    }

    private static HashMap<UHCPlayer, Integer> convertVotesToMap(Vote[] votes) {
        HashMap<UHCPlayer, Integer> hm = new HashMap<>();
        for (Vote key : votes) {
            hm.put(key.getVoted(), hm.getOrDefault(key.getVoted(), 0) + 1);
        }
        return hm;
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        super.tick(gameTimer, tick);
        if (gameTimer.getGame().equals(game)) {
            if (isActive()) {
                if (tick >= minGapToVote * timeBetweenVotes * 20) {
                    if (tick % (timeBetweenVotes * 20) == 0) {
                        votes.clear();
                        Bukkit.getPluginManager().callEvent(new VoteStartEvent(game, this));
                        proceedPreVote();
                    }
                    if (tick % ((timeBetweenVotes * 20)) == 0) {
                        VoteEndEvent voteEndEvent = new VoteEndEvent(game, convertMapToArray(votes), this);
                        Bukkit.getPluginManager().callEvent(voteEndEvent);
                        if (voteEndEvent.isCancelled()) {
                            votes.clear();
                        }
                    }
                    if (tick % ((timeBetweenVotes * 20) + (timeBeforeAnnounce * 20)) == 0) {
                        VoteAnnouncementEvent voteEndEvent = new VoteAnnouncementEvent(game, convertMapToArray(votes), this);
                        Bukkit.getPluginManager().callEvent(voteEndEvent);
                        if (!voteEndEvent.isCancelled()) {
                            proceedVote(votes);
                        }
                        votes.clear();
                    }
                }
            }
        }
    }

    @ScenarioCommand
    public void onCommand(CommandUtilis command) {
        try {
            if (command.executeCommand("uhc.vote", "", 1, CommandUtilis.Checks.GAME)) {
                UHCPlayer sender = UHCPlayer.thePlayer((Player) command.getSender());
                if (playerCanVotes) {
                    UHCPlayer target = game.getUHCPlayerInThisGame(command.getArgs()[1]);
                    if (target != null) {
                        if (target.isPlaying() && target.getGameData().getGame().equals(sender.getGameData().getGame())) {
                                boolean hasVoted = false;
                                for (Map.Entry<Vote, Integer> entry : this.getVotes().entrySet()) {
                                    Vote vote = entry.getKey();
                                    if (vote.isSameVoter(sender))
                                        hasVoted = true;
                                }
                                if (!hasVoted) {
                                    this.getVotes().put(new Vote(sender, target), 1);
                                    sender.sendMessage(UHCCore.getPrefix() + ChatColor.DARK_GREEN + " Votre vote a bien été comptabilisé.");
                                } else {
                                    sender.sendMessage(UHCCore.getPrefix() + ChatColor.DARK_RED + " Vous avez dejà voté !");
                                }
                        } else {
                            sender.sendMessage(UHCCore.getPrefix() + ChatColor.DARK_RED + " Vous ne pouvez pas voter cette personne");
                        }
                    } else {
                        sender.sendMessage(UHCCore.getPrefix() + ChatColor.DARK_RED + " Cette personne n'existe pas");
                    }
                }
            }
        } catch (NoPermissionException | NoArgsException e) {

        }
    }

    private void proceedPreVote() {
        if (checkCanVote(minPlayerToVote)) {
            sendPreVoteMessage(timeToVote * 20);
            authoriseVote();
        } else {
            for (UHCPlayer deadAndAlivePlayer : game.getSpectatorsAndAlivePlayers()) {
                try {
                    deadAndAlivePlayer.sendMessage(deadAndAlivePlayer.getLanguage().getMessage(String.format(getMessagesId() + "deactivated", minPlayerToVote)));
                } catch (CannotFindMessageException e) {
                    deadAndAlivePlayer.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot find message " + e.getMessage() + " for language " + e.getLanguage().getId());
                    Logger.getGeneralLogger().log(e);
                }
            }
        }
    }

    private void authoriseVote() {
        playerCanVotes = true;
    }

    private void unAuthoriseVoteFor(Set<UHCPlayer> players) {
        playerCanVotes = false;
    }

    private void proceedVote(Vote[] votes) {
        if (!checkCanVote(minPlayerToVote)) {
            return;
        }

        UHCPlayer result = getPlayerWithMostVote(votes);
        Integer mostVotes = getMostVote(votes);

        Chat.broadcastIdMessage(getMessagesId() + "results.header", game.getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
        if (mostVotes != 0) {
            sendResults(result.getName(), mostVotes);
            applyResults(result);
        } else
            sendNullResults();
    }

    private void proceedVote(Map<Vote, Integer> votes) {
        if (!checkCanVote(minPlayerToVote)) {
            return;
        }

        if (!votes.isEmpty())
            proceedVote(convertMapToArray(votes));
        else {
            Chat.broadcastIdMessage(getMessagesId() + "results.header", game.getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
            sendNullResults();
        }
        unAuthoriseVoteFor(game.getAlivePlayers());
    }

    private boolean checkCanVote(int maxSize) {
        return game.getSpectatorsAndAlivePlayers().size() >= maxSize;
    }

    private void sendPreVoteMessage(int time) {
        int heures = (time / 20 / 3600);
        int minutes = ((time / 20 - (time / 20 / 3600) * 3600) / 60);
        int seconds = time / 20 - (heures * 3600 + minutes * 60);

        if (heures == 0) {
            if (minutes == 0) {
                if (seconds == 0) {
                    Logger.getGeneralLogger().log("Erreur de paramatres");
                } else {
                    for (UHCPlayer lgp : game.getSpectatorsAndAlivePlayers()) {
                        try {
                            lgp.sendMessage(getMessage(lgp.getLanguage(), "message.seconds").replace("%voteSeconds%", String.valueOf(seconds)));
                        } catch (CannotFindMessageException e) {
                            lgp.sendMessage(Language.NO_MESSAGE);
                            Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot fond message " + e.getMessage() + " for language " + e.getLanguage().getId());

                        }
                    }
                }
            } else {
                if (seconds == 0) {
                    for (UHCPlayer lgp : game.getSpectatorsAndAlivePlayers()) {
                        try {
                            lgp.sendMessage(getMessage(lgp.getLanguage(), "message.minutes").replace("%voteMinutes%", String.valueOf(minutes)));
                        } catch (CannotFindMessageException e) {
                            lgp.sendMessage(Language.NO_MESSAGE);
                            Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot fond message " + e.getMessage() + " for language " + e.getLanguage().getId());
                        }
                    }
                } else {
                    for (UHCPlayer lgp : game.getSpectatorsAndAlivePlayers()) {
                        try {
                            lgp.sendMessage(getMessage(lgp.getLanguage(), "message.minutesSeconds").replace("%voteMinutes%", String.valueOf(minutes)).replace("%voteSeconds%", String.valueOf(seconds)));
                        } catch (CannotFindMessageException e) {
                            lgp.sendMessage(Language.NO_MESSAGE);
                            Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot fond message " + e.getMessage() + " for language " + e.getLanguage().getId());
                        }
                    }
                }
            }
        } else {
            Logger.getGeneralLogger().logInConsole("Wrong vote time : time is more than 1 hour");
            minutes = 59;
            seconds = 59;
            for (UHCPlayer lgp : game.getSpectatorsAndAlivePlayers()) {
                try {
                    lgp.sendMessage(getMessage(lgp.getLanguage(), "message.minutesSeconds").replace("%voteMinutes%", String.valueOf(minutes)).replace("%voteSeconds%", String.valueOf(seconds)));
                } catch (CannotFindMessageException e) {
                    lgp.sendMessage(Language.NO_MESSAGE);
                    Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot fond message " + e.getMessage() + " for language " + e.getLanguage().getId());
                }
            }
        }
    }

    private void sendResults(String name, int votes) {
        for (UHCPlayer lgp : game.getSpectatorsAndAlivePlayers()) {
            try {
                lgp.sendMessage(getMessage(lgp.getLanguage(), "results.header").replace("%votedName%", name).replace("%numberVotes%", String.valueOf(votes)));
            } catch (CannotFindMessageException e) {
                lgp.sendMessage(Language.NO_MESSAGE);
                Logger.getGeneralLogger().logInConsole(ChatColor.DARK_RED + "Cannot fond message " + e.getMessage() + " for language " + e.getLanguage().getId());
            }
            lgp.getPlayer().playSound(lgp.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT, SoundCategory.AMBIENT, 1f, 1f);
        }
    }

    private void sendNullResults() {
        Chat.broadcastIdMessage(getMessagesId() + "null", game.getSpectatorsAndAlivePlayers().toArray(new UHCPlayer[0]));
    }

    private void applyResults(UHCPlayer lgp) {
        lgp.getPlayerStats().refresh();
        lgp.getPlayerStats().setMaxHealth(lgp.getPlayerStats().getMaxHealth() / 2);
        lgp.getPlayerStats().updateMaxHealth();
    }

    public HashMap<Vote, Integer> getVotes() {
        return votes;
    }

    @ScenarioGetter(name = "Minimum players")
    public int getMinPlayerToVote() {
        return minPlayerToVote;
    }

    @ScenarioSetter(name = "Minimum players")
    public void setMinPlayerToVote(int minPlayerToVote) {
        this.minPlayerToVote = minPlayerToVote;
    }

    @ScenarioGetter(name = "Minimum time")
    public int getMinGapToVote() {
        return minGapToVote;
    }

    @ScenarioSetter(name = "Minimum time")
    public void setMinGapToVote(int minGapToVote) {
        this.minGapToVote = minGapToVote;
    }

    @ScenarioGetter(name = "Time to vote")
    public int getTimeToVote() {
        return timeToVote;
    }

    @ScenarioSetter(name = "Time to vote")
    public void setTimeToVote(int timeToVote) {
        this.timeToVote = timeToVote;
    }


    @ScenarioGetter(name = "Time between votes")
    public int getTimeBetweenVotes() {
        return timeBetweenVotes;
    }

    @ScenarioSetter(name = "Time between votes")
    public void setTimeBetweenVotes(int timeBetweenVotes) {
        this.timeBetweenVotes = timeBetweenVotes;
    }

    @ScenarioGetter(name = "Time before announce")
    public int getTimeBeforeAnnounce() {
        return timeBeforeAnnounce;
    }

    @ScenarioSetter(name = "Time before announce")
    public void setTimeBeforeAnnounce(int timeBeforeAnnounce) {
        this.timeBeforeAnnounce = timeBeforeAnnounce;
    }

    public boolean isPlayerCanVotes() {
        return playerCanVotes;
    }
}
