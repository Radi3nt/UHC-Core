package fr.radi3nt.loupgarouuhc.classes.lang.translations.lang;

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

        message = message.replace(getFormatedPlaceHolder("paramVoteMinPlayer"), String.valueOf(player.getGame().getParameters().getMinPlayerForVotes()));
        message = message.replace(getFormatedPlaceHolder("roleName"), player.getRole().getName(player.getLanguage()));
        message = message.replace(getFormatedPlaceHolder("roleDesc"), player.getRole().getDescription(player.getLanguage()));
        message = message.replace(getFormatedPlaceHolder("roleShortDesc"), player.getRole().getShortDescription(player.getLanguage()));
        message = message.replace(getFormatedPlaceHolder("gameTimerDays"), String.valueOf(player.getGame().getGameTimer().getDays()));


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
