package fr.radi3nt.loupgarouuhc.classes.stats;

import java.text.DecimalFormat;

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
            double percentageOfWin=(float)winnedGames/gameNumber*100;
            try {
                String percentageS = new DecimalFormat("###.#").format(percentageOfWin);
                percentageOfWin = Float.parseFloat(percentageS.trim());
            } catch (NumberFormatException e) {

            }
            return percentageOfWin;
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
