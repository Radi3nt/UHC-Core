package fr.radi3nt.uhc.api.scenarios.scenario.vote;

import fr.radi3nt.uhc.api.player.UHCPlayer;

import java.util.Objects;

public class Vote {

    private UHCPlayer voted;
    private UHCPlayer voter;

    public Vote(UHCPlayer voter, UHCPlayer voted) {
        this.voter = voter;
        this.voted = voted;
    }

    public boolean isSameVoter(UHCPlayer lgPlayer) {
        return this.getVoter().equals(lgPlayer);
    }

    public UHCPlayer getVoted() {
        return voted;
    }

    public void setVoted(UHCPlayer voted) {
        this.voted = voted;
    }

    public UHCPlayer getVoter() {
        return voter;
    }

    public void setVoter(UHCPlayer voter) {
        this.voter = voter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(voted, vote.voted) &&
                Objects.equals(voter, vote.voter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voted, voter);
    }
}
