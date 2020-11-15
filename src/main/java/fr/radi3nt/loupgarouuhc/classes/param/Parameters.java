package fr.radi3nt.loupgarouuhc.classes.param;

import fr.radi3nt.uhc.api.game.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Parameters implements Parameter {


    private boolean autoMap = false;
    private boolean recreateMapAfterUse = false;

    private int dayRoleDivulged = 2;
    private int timeMultiplication = 1;

    private boolean hiddenCompo = false;

    private boolean isTroll = false;
    private boolean isRandomTroll = true;
    private int trollEndTime = 3 * 60 * 20 + 20 * 60 * 20;

    private boolean onlyBlockCanBeRemoved = false;
    private boolean preserveRareItem = true;
    private int numberOfBlockRemovedWhenRespawn = 30;
    private int percentageOfChanceToRemove = 40;

    private boolean respawnBeforeRoleDivulged = true;
    private boolean canReconnectInPvp = true;
    private int disconnectTimeout = 5 * 60 * 20;

    private int baseRadius = 1000;

    private Location spawn = new Location(Bukkit.getWorlds().get(0), 0.5, 133, 0.5, 160, 0);
    private Location gameSpawn = new Location(Bukkit.getWorlds().get(0), 0, 100, 0);


    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getGameSpawn() {
        return gameSpawn;
    }

    public void setGameSpawn(Location gameSpawn) {
        this.gameSpawn = gameSpawn;
    }

    public Integer getBaseRadius() {
        return baseRadius;
    }

    public void setBaseRadius(Integer baseRadius) {
        this.baseRadius = baseRadius;
    }

    public int getDayRoleDivulged() {
        return dayRoleDivulged;
    }

    public void setDayRoleDivulged(int dayRoleDivulged) {
        this.dayRoleDivulged = dayRoleDivulged;
    }

    public int getTimeMultiplication() {
        return timeMultiplication;
    }

    public void setTimeMultiplication(int timeMultiplication) {
        this.timeMultiplication = timeMultiplication;
    }

    public int getDisconnectTimeout() {
        return disconnectTimeout;
    }

    public void setDisconnectTimeout(int disconnectTimeout) {
        this.disconnectTimeout = disconnectTimeout;
    }

    public boolean isRespawnBeforeRoleDivulged() {
        return respawnBeforeRoleDivulged;
    }

    public void setRespawnBeforeRoleDivulged(boolean respawnBeforeRoleDivulged) {
        this.respawnBeforeRoleDivulged = respawnBeforeRoleDivulged;
    }

    public int getNumberOfBlockRemovedWhenRespawn() {
        return numberOfBlockRemovedWhenRespawn;
    }

    public void setNumberOfBlockRemovedWhenRespawn(int numberOfBlockRemovedWhenRespawn) {
        this.numberOfBlockRemovedWhenRespawn = numberOfBlockRemovedWhenRespawn;
    }

    public int getPercentageOfChanceToRemove() {
        return percentageOfChanceToRemove;
    }

    public void setPercentageOfChanceToRemove(int percentageOfChanceToRemove) {
        this.percentageOfChanceToRemove = percentageOfChanceToRemove;
    }

    public boolean isOnlyBlockCanBeRemoved() {
        return onlyBlockCanBeRemoved;
    }

    public void setOnlyBlockCanBeRemoved(boolean onlyBlockCanBeRemoved) {
        this.onlyBlockCanBeRemoved = onlyBlockCanBeRemoved;
    }

    public boolean isPreserveRareItem() {
        return preserveRareItem;
    }

    public void setPreserveRareItem(boolean preserveRareItem) {
        this.preserveRareItem = preserveRareItem;
    }

    public boolean isAutoMap() {
        return autoMap;
    }

    public void setAutoMap(boolean autoMap) {
        this.autoMap = autoMap;
    }

    public boolean isRecreateMapAfterUse() {
        return recreateMapAfterUse;
    }

    public void setRecreateMapAfterUse(boolean recreateMapAfterUse) {
        this.recreateMapAfterUse = recreateMapAfterUse;
    }

    public boolean isHiddenCompo() {
        return hiddenCompo;
    }

    public void setHiddenCompo(boolean hiddenCompo) {
        this.hiddenCompo = hiddenCompo;
    }

    public boolean isCanReconnectInPvp() {
        return canReconnectInPvp;
    }

    public void setCanReconnectInPvp(boolean canReconnectInPvp) {
        this.canReconnectInPvp = canReconnectInPvp;
    }

    public boolean isTroll() {
        return isTroll;
    }

    public void setTroll(boolean troll) {
        isTroll = troll;
    }

    public int getTrollEndTime() {
        return trollEndTime;
    }

    public void setTrollEndTime(int trollEndTime) {
        this.trollEndTime = trollEndTime;
    }

    public boolean isRandomTroll() {
        return isRandomTroll;
    }

    public void setRandomTroll(boolean randomTroll) {
        isRandomTroll = randomTroll;
    }
}
