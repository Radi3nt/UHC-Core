package fr.radi3nt.loupgarouuhc.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.loupgarouuhc.event.events.VoteAnnouncementEvent;
import fr.radi3nt.loupgarouuhc.event.events.VoteEndEvent;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import fr.radi3nt.loupgarouuhc.roles.WinType;

import fr.radi3nt.loupgarouuhc.roles.roles.attributes.LimitedUse;
import fr.radi3nt.loupgarouuhc.roles.roles.attributes.Power;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;

public class Citoyen extends Role implements Power, LimitedUse {

    private int use = 2;
    private boolean power = true;
    private boolean canceled = false;
    private boolean canCancel = false;

    public Citoyen(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("Citoyen", WinType.VILLAGE, RoleType.VILLAGER);
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    protected void night(LGGame game, UHCPlayer lgp) {

    }

    @Override
    protected void day(LGGame game, UHCPlayer lgp) {

    }

    @Override
    protected void newEpisode(LGGame game, UHCPlayer lgp) {

    }

    @Override
    protected void killSomeone(LGGame game, UHCPlayer killer, UHCPlayer killed) {

    }

    @Override
    protected void killed(LGGame game, UHCPlayer killed, UHCPlayer killer, Location location) {

    }

    @Override
    protected void discoverRole(LGGame game, UHCPlayer lgp) {

    }

    @EventHandler
    public void onEndVote(VoteEndEvent e) {
        if (e.getGame()==getGame()) {
            for (UHCPlayer gamePlayer : e.getGame().getGamePlayers()) {
                if (gamePlayer.getGameData().getRole().equals(this)) {
                    if (game.getGamePlayers().size()>=e.getScenario().getMinPlayerToVote())
                        if (getUse()!=0)
                            gamePlayer.sendMessage(LoupGarouUHC.getPrefix() + " " + LoupGarouUHC.getPrefixPrivé() + ChatColor.BLUE + " Tu a 10 secondes pour voir les votes (/lg role see) et choisir de les annuler ou pas");
                }
            }
            canCancel=true;
        }
    }

    @EventHandler
    public void onEndVote(VoteAnnouncementEvent e) {
        if (e.getGame()==getGame()) {
            if (canceled) {
                e.setCancelled(true);
                canceled=false;
                for (UHCPlayer gamePlayer : e.getGame().getGamePlayers()) {
                    gamePlayer.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.GOLD + " Le vote à été annulé par le citoyen"); //todo trad
                }
            }
            canCancel=false;
        }
    } //todo auto enable vote

    public void cancelVote() {
        canceled=true;
        canCancel=false;
    }


    @Override
    public void setPower(Boolean p0) {
        power=p0;
    }

    @Override
    public Boolean hasPower() {
        return power;
    }

    @Override
    public int getUse() {
        return use;
    }

    @Override
    public void setUse(int p0) {
        use=p0;
    }

    public boolean isCanCancel() {
        return canCancel;
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }
}
