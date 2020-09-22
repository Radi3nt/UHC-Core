package fr.radi3nt.loupgarouuhc.classes.param;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;

public class Parameters {


    HashMap<Integer, Integer> worldBorderTime = new HashMap<>();
    private int pvpActivate = 30*60*20;
    private int dayRoleDivulged = 2;
    private int minDayForVote = 3;
    private int getTimeForVote = 60*20;
    private int percentageOfAppleDrop = 20;
    private int percentageOfGravelDrop = 20;
    private int timeMultiplication = 1;
    private int diamondLimit = 17;
    private int finalHealTime = (20*60*20)-getTimeMultiplication();
    private boolean finalHeal = true;

    private boolean respawnBeforeRoleDivulged = true;
    private boolean onlyBlockCanBeRemoved = false;
    private boolean preserveRareItem = true;
    private int numberOfBlockRemovedWhenRespawn = 50;
    private int percentageOfChanceToRemove = 90;

    private int disconnectTimeout = 20*60*20;
    private int minPLayerForVotes = 4;

    private boolean cutClean = false;

    private Location spawn = new Location(Bukkit.getWorlds().get(0), 0.5, 133, 0.5, 160, 0);
    private Location gameSpawn = new Location(Bukkit.getWorlds().get(0), 0, 100, 0);
    private Integer baseRadius = 1000;

    public HashMap<Integer, Integer> getWorldBorderTime() {
        return worldBorderTime;
    }

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

    public int getPvpActivate() {
        return pvpActivate;
    }

    public void setPvpActivate(int pvpActivate) {
        this.pvpActivate = pvpActivate;
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

    public int getPercentageOfAppleDrop() {
        return percentageOfAppleDrop;
    }

    public void setPercentageOfAppleDrop(int percentageOfAppleDrop) {
        this.percentageOfAppleDrop = percentageOfAppleDrop;
    }
    public int getTimeMultiplication() {
        return timeMultiplication;
    }

    public void setTimeMultiplication(int timeMultiplication) {
        this.timeMultiplication = timeMultiplication;
    }

    public boolean isCutClean() {
        return cutClean;
    }

    public void setCutClean(boolean cutClean) {
        this.cutClean = cutClean;
    }

    public int getDiamondLimit() {
        return diamondLimit;
    }

    public void setDiamondLimit(int diamondLimit) {
        this.diamondLimit = diamondLimit;
    }

    public int getFinalHealTime() {
        return finalHealTime;
    }

    public void setFinalHealTime(int finalHealTime) {
        this.finalHealTime = finalHealTime;
    }

    public boolean isFinalHeal() {
        return finalHeal;
    }

    public void setFinalHeal(boolean finalHeal) {
        this.finalHeal = finalHeal;
    }

    public int getPercentageOfGravelDrop() {
        return percentageOfGravelDrop;
    }

    public void setPercentageOfGravelDrop(int percentageOfGravelDrop) {
        this.percentageOfGravelDrop = percentageOfGravelDrop;
    }

    public int getDisconnectTimeout() {
        return disconnectTimeout;
    }

    public void setDisconnectTimeout(int disconnectTimeout) {
        this.disconnectTimeout = disconnectTimeout;
    }

    public int getMinPlayerForVotes() {
        return minPLayerForVotes;
    }

    public void setMinPLayerForVotes(int minPLayerForVotes) {
        this.minPLayerForVotes = minPLayerForVotes;
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
}
