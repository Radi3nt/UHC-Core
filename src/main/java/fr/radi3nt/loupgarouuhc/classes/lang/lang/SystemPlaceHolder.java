package fr.radi3nt.loupgarouuhc.classes.lang.lang;

import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;

import java.util.ArrayList;

public class SystemPlaceHolder extends PlaceHolder {

    private static final ArrayList<SystemPlaceHolder> systemPlaceHolders = new ArrayList<>();
    private static final char idChar = '%';

    private final LGPlayer player;

    public SystemPlaceHolder(String id, String message, LGPlayer player) {
        super(id, message);
        this.player = player;
        systemPlaceHolders.add(this);
    }

    private static String getFormatedPlaceHolder(String id) {
        return idChar + id + idChar;
    }

    public static String getMessageReplaced(String message, LGPlayer player) {

        if (player != null) {
            if (player.isInGame()) {
                if (player.getGameData().hasRole()) {
                    message = message.replace(getFormatedPlaceHolder("roleName"), player.getGameData().getRole().getName(player.getLanguage()));
                    message = message.replace(getFormatedPlaceHolder("roleDesc"), player.getGameData().getRole().getDescription(player.getLanguage()));
                    message = message.replace(getFormatedPlaceHolder("roleShortDesc"), player.getGameData().getRole().getShortDescription(player.getLanguage()));

                }
                if (player.getGameData().getGame().getGameTimer() != null) {
                    message = message.replace(getFormatedPlaceHolder("paramVoteMinPlayer"), String.valueOf(player.getGameData().getGame().getParameters().getMinPlayerForVotes()));
                    message = message.replace(getFormatedPlaceHolder("gameTimerDays"), String.valueOf(player.getGameData().getGame().getGameTimer().getDays()));
                    message = message.replace(getFormatedPlaceHolder("gameTimerDays-"), String.valueOf(player.getGameData().getGame().getGameTimer().getDays() - 1));
                    int ticksPvp = player.getGameData().getGame().getPvP().getTime() - player.getGameData().getGame().getGameTimer().getTicks();
                    int heures1 = (ticksPvp / 20 / 3600);
                    int minutes1 = ((ticksPvp / 20 - (ticksPvp / 20 / 3600) * 3600) / 60);
                    int seconds1 = ticksPvp / 20 - (heures1 * 3600 + minutes1 * 60);
                    message = message.replace(getFormatedPlaceHolder("pvpSeconds-"), String.valueOf(seconds1));
                    message = message.replace(getFormatedPlaceHolder("pvpMinutes-"), String.valueOf(minutes1));
                    message = message.replace(getFormatedPlaceHolder("pvpHours-"), String.valueOf(heures1));
                }
            }
        }
        message = message.replace(getFormatedPlaceHolder("author"), "Radi3nt");

        return message;
    }

    @Override
    public String getPlaceHolder() {
        return idChar + getId() + idChar;
    }

    public LGPlayer getPlayer() {
        return player;
    }
}
