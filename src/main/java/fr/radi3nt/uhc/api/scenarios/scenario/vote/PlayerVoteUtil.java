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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerVoteUtil {

    public static HashMap<UHCPlayer, Integer> convertVotesToMap(Vote[] votes) {
        HashMap<UHCPlayer, Integer> hm = new HashMap<>();
        for (Vote key : votes) {
            hm.put(key.getVoted(), hm.getOrDefault(key.getVoted(), 0) + 1);
        }
        return hm;
    }

    public static Vote[] convertMapToArray(Map<Vote, Integer> votes) {
        ArrayList<Vote> votesArray = new ArrayList<>();
        for (Map.Entry<Vote, Integer> voteIntegerEntry : votes.entrySet()) {
            for (int integer = 0; integer < voteIntegerEntry.getValue(); integer++) {
                votesArray.add(voteIntegerEntry.getKey());
            }
        }
        return votesArray.toArray(new Vote[0]);
    }

    public static UHCPlayer getPlayerWithMostVote(Vote[] votes) {
        HashMap<UHCPlayer, Integer> hm = convertVotesToMap(votes);
        UHCPlayer result = null;
        for (Map.Entry<UHCPlayer, Integer> entry : hm.entrySet()) {
            if (entry.getValue().equals(getMostVote(votes))) {
                result = entry.getKey();
            }
        }
        return result;
    }

    public static Integer getMostVote(Vote[] votes) {
        HashMap<UHCPlayer, Integer> hm = convertVotesToMap(votes);
        int max = 0;
        for (Map.Entry<UHCPlayer, Integer> entry : hm.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
            }
        }
        return max;
    }

    public static UHCPlayer getPlayerWithMostVote(Map<Vote, Integer> votes) {
        return getPlayerWithMostVote(convertMapToArray(votes));
    }
}
