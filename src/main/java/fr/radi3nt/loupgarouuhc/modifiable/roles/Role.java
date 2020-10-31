package fr.radi3nt.loupgarouuhc.modifiable.roles;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.lang.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public abstract class Role {

    private static final HashMap<RoleIdentity, Constructor<? extends Role>> roleLinkByStringKey = new HashMap<>();

    private LGGame game;
    private WinType winType;
    private RoleType roleType;

    public Role(LGGame game) {
        this.game = game;
        this.winType = this.getRoleIdentity().getWinType();
        this.roleType = this.getRoleIdentity().getRoleType();
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return null;
    }

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

    public abstract RoleIdentity getRoleIdentity();

    public String getName(Languages languages) {
        return this.getRoleIdentity().getName(languages);
    }

    public String getShortDescription(Languages languages) {
        return this.getRoleIdentity().getShortDescription(languages);
    }

    public void OnNight(LGGame game, LGPlayer lgp) {
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

    protected abstract void night(LGGame game, LGPlayer lgp);

    public void OnDay(LGGame game, LGPlayer lgp) {
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

    protected abstract void day(LGGame game, LGPlayer lgp);

    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayerStats().refresh();
        lgp.getPlayerStats().setMaxHealth(this.getRoleIdentity().getMaxHealth());
        lgp.getPlayerStats().update();
        newEpisode(game, lgp);
    }

    protected abstract void newEpisode(LGGame game, LGPlayer lgp);

    public void OnkillSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {
        killSomeone(game, killer, killed);
    }

    protected abstract void killSomeone(LGGame game, LGPlayer killer, LGPlayer killed);

    public void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {
        killed(game, killed, killer, location);
    }

    protected abstract void killed(LGGame game, LGPlayer killed, LGPlayer killer, Location location);

    public void OnDiscoverRole(LGGame game, LGPlayer lgp) {
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

    protected abstract void discoverRole(LGGame game, LGPlayer lgp);

    public void join(LGPlayer player, boolean sendMessage) {
        player.getGameData().setRole(this);
        if (sendMessage) {
            Logger.getGeneralLogger().logInConsole(player.getName() + " est " + this.getName(player.getLanguage()));
            player.getGameData().getGame().getData().getLogChat().log(player.getName() + " est " + this.getName(player.getLanguage()));
            displayRole(player);
        }
    }

    public void displayRole(LGPlayer player) {
        player.sendTitle((player.getLanguage().getMessage("joinRoleTitleTitle", player)), (player.getLanguage().getMessage("joinRoleTitleSubtitle", player)), 10, 200, 10);
        player.sendMessage(player.getLanguage().getMessage("joinRoleMessage", player));
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
}
