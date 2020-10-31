package fr.radi3nt.loupgarouuhc.classes.param;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;

public class Parameters {


    private HashMap<Integer, Integer> worldBorderTime = new HashMap<>();


    private boolean autoMap = false;
    private boolean recreateMapAfterUse = false;

    private int dayRoleDivulged = 2;
    private int minDayForVote = 3;
    private int getTimeForVote = 60 * 20;
    private int timeMultiplication = 1;

    private boolean hiddenCompo = false;
    private boolean isTroll = false;
    private boolean isRandomTroll = true;
    private int trollEndTime = 3 * 60 * 20 + 20 * 60 * 20;
    private boolean canReconnectInPvp = true;
    private boolean respawnBeforeRoleDivulged = true;
    private boolean onlyBlockCanBeRemoved = false;
    private boolean preserveRareItem = true;
    private int numberOfBlockRemovedWhenRespawn = 30;
    private int percentageOfChanceToRemove = 40;

    private int disconnectTimeout = 5 * 60 * 20;
    private int minPlayerForVotes = 3;

    private Location spawn = new Location(Bukkit.getWorlds().get(0), 0.5, 133, 0.5, 160, 0);
    private Location gameSpawn = new Location(Bukkit.getWorlds().get(0), 0, 100, 0);
    private Integer baseRadius = 1000;


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

    public int getMinDayForVote() {
        return minDayForVote;
    }

    public void setMinDayForVote(int minDayForVote) {
        this.minDayForVote = minDayForVote;
    }

    public int getGetTimeForVote() {
        return getTimeForVote;
    }

    public void setGetTimeForVote(int getTimeForVote) {
        this.getTimeForVote = getTimeForVote;
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

    public int getMinPlayerForVotes() {
        return minPlayerForVotes;
    }

    public void setMinPlayerForVotes(int minPlayerForVotes) {
        this.minPlayerForVotes = minPlayerForVotes;
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

    public HashMap<Integer, Integer> getWorldBorderTime() {
        return worldBorderTime;
    }

    public void setWorldBorderTime(HashMap<Integer, Integer> worldBorderTime) {
        this.worldBorderTime = worldBorderTime;
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
