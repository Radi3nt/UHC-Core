package fr.radi3nt.uhc.api.stats;

import fr.radi3nt.uhc.api.events.stats.StatsPointChanges;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Stats {

    private Integer gameNumber = 0;
    private Integer winnedGames = 0;
    private Integer kills = 0;
    private Integer points = 0;

    public Integer getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(Integer gameNumber) {
        this.gameNumber = gameNumber;
    }

    public Integer getWinnedGames() {
        return winnedGames;
    }

    public void setWinnedGames(Integer winnedGames) {
        this.winnedGames = winnedGames;
    }

    public double getPercentageOfWin() {
        try {
            if (!Double.isNaN((float) winnedGames / gameNumber * 100)) {
                BigDecimal percentageOfWin = new BigDecimal((float) winnedGames / gameNumber * 100);
                percentageOfWin = percentageOfWin.setScale(3, RoundingMode.HALF_DOWN);
                return percentageOfWin.doubleValue();
            } else {
                return (float) winnedGames / gameNumber * 100;
            }
        } catch (ArithmeticException e) {
            return 0;
        }
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
