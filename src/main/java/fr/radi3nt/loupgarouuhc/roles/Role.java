package fr.radi3nt.loupgarouuhc.roles;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.lang.lang.Languages;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public abstract class Role implements Listener {

    private static final HashMap<RoleIdentity, Constructor<? extends Role>> roleLinkByStringKey = new HashMap<>();

    protected LGGame game;
    private WinType winType;
    private RoleType roleType;
    private boolean infected = false;

    public Role(LGGame game) {
        this.game = game;
        this.winType = this.getRoleIdentity().getWinType();
        this.roleType = this.getRoleIdentity().getRoleType();
    }

    protected abstract void night(LGGame game, UHCPlayer lgp);

    public void OnNight(LGGame game, UHCPlayer lgp) {
        lgp.getPlayerStats().refresh();
        for (PotionEffect potionEffect : this.getRoleIdentity().getPotionEffectsDay()) {
            lgp.getPlayerStats().removePotionEffect(potionEffect.getType());
        }
        for (PotionEffect potionEffect : this.getRoleIdentity().getPotionPermanant()) {
            lgp.getPlayerStats().addPotionEffect(potionEffect);
        }
        for (PotionEffect potionEffect : this.getRoleIdentity().getPotionEffectsNight()) {
            lgp.getPlayerStats().addPotionEffect(potionEffect);
        }
        lgp.getPlayerStats().update();
        night(game, lgp);
    }


    protected abstract void day(LGGame game, UHCPlayer lgp);

    public void OnDay(LGGame game, UHCPlayer lgp) {
        lgp.getPlayerStats().refresh();
        for (PotionEffect potionEffect : this.getRoleIdentity().getPotionEffectsNight()) {
            lgp.getPlayerStats().removePotionEffect(potionEffect.getType());
        }
        for (PotionEffect potionEffect : this.getRoleIdentity().getPotionPermanant()) {
            lgp.getPlayerStats().addPotionEffect(potionEffect);
        }
        for (PotionEffect potionEffect : this.getRoleIdentity().getPotionEffectsDay()) {
            lgp.getPlayerStats().addPotionEffect(potionEffect);
        }
        lgp.getPlayerStats().update();
        day(game, lgp);
    }

    protected abstract void newEpisode(LGGame game, UHCPlayer lgp);

    public void OnNewEpisode(LGGame game, UHCPlayer lgp) {
        lgp.getPlayerStats().refresh();
        lgp.getPlayerStats().setMaxHealth(this.getRoleIdentity().getMaxHealth());
        lgp.getPlayerStats().update();
        newEpisode(game, lgp);
    }


    protected abstract void killSomeone(LGGame game, UHCPlayer killer, UHCPlayer killed);

    public void OnkillSomeone(LGGame game, UHCPlayer killer, UHCPlayer killed) {
        killSomeone(game, killer, killed);
    }


    protected abstract void killed(LGGame game, UHCPlayer killed, UHCPlayer killer, Location location);

    public void OnKilled(LGGame game, UHCPlayer killed, UHCPlayer killer, Location location) {
        killed(game, killed, killer, location);
    }

    public void OnDiscoverRole(LGGame game, UHCPlayer lgp) {
        for (ItemStack roleItem : this.getRoleIdentity().getRoleItems()) {
            lgp.getPlayerStats().refresh();
            if (lgp.getPlayerStats().getInventory().firstEmpty() == -1) {
                lgp.getPlayerStats().getLastLocation().getChunk().load();
                lgp.getPlayerStats().getLastLocation().getWorld().dropItem(lgp.getPlayerStats().getLastLocation(), roleItem);
            } else {
                lgp.getPlayerStats().getInventory().addItem(roleItem);
            }
            lgp.getPlayerStats().update();
        }
        lgp.getPlayerStats().refresh();
        for (PotionEffect potionEffect : this.getRoleIdentity().getPotionPermanant()) {
            lgp.getPlayerStats().addPotionEffect(potionEffect);
        }
        lgp.getPlayerStats().update();
        discoverRole(game, lgp);
    }

    protected abstract void discoverRole(LGGame game, UHCPlayer lgp);

    public void join(UHCPlayer player, boolean sendMessage) {
        player.getGameData().setRole(this);
        if (sendMessage) {
            Logger.getGeneralLogger().logInConsole(player.getName() + " est " + this.getName(player.getLanguage()));
            player.getGameData().getGame().getData().getLogChat().log(player.getName() + " est " + this.getName(player.getLanguage()));
            displayRole(player);
        }
    }

    public void displayRole(UHCPlayer player) {
        player.sendTitle((player.getLanguage().getMessage("joinRoleTitleTitle", player)), (player.getLanguage().getMessage("joinRoleTitleSubtitle", player)), 10, 200, 10);
        player.sendMessage(player.getLanguage().getMessage("joinRoleMessage", player));
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return null;
    }
    public abstract RoleIdentity getRoleIdentity();

    public WinType getWinType() {
        return this.winType;
    }

    public void setWinType(WinType winType) {
        this.winType = winType;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public static HashMap<RoleIdentity, Constructor<? extends Role>> getRoleLinkByStringKey() {
        return roleLinkByStringKey;
    }

    public String getName(Languages languages) {
        return this.getRoleIdentity().getName(languages);
    }

    public String getShortDescription(Languages languages) {
        return this.getRoleIdentity().getShortDescription(languages);
    }

    public LGGame getGame() {
        return game;
    }

    public void setGame(LGGame game) {
        this.game = game;
    }

    public String getDescription(Languages languages) {
        return this.getRoleIdentity().getRoleDescription(languages);
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }
}
