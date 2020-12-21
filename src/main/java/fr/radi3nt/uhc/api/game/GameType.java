package fr.radi3nt.uhc.api.game;

import java.util.Objects;

public class GameType {

    private final Class<? extends UHCGame> gameClass;
    private final Class<? extends Parameter> parametersClass;
    private final String displayName;
    private final String name;
    private final String ID;

    public GameType(Class<? extends UHCGame> gameClass, Class<? extends Parameter> parametersClass, String displayName, String name, String ID) {
        this.gameClass = gameClass;
        this.parametersClass = parametersClass;
        this.displayName = displayName;
        this.name = name;
        this.ID = ID;
    }

    public Class<? extends UHCGame> getGameClass() {
        return gameClass;
    }

    public Class<? extends Parameter> getParametersClass() {
        return parametersClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameType gameType = (GameType) o;
        return Objects.equals(ID, gameType.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
